package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sdd.entities.*;
import com.sdd.entities.repository.*;
import com.sdd.exception.SDDException;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.request.AppointmentCancelRequest;
import com.sdd.request.MainFormRequest;
import com.sdd.request.RenewalFormRequest;
import com.sdd.request.UpdatePaymentRequest;
import com.sdd.response.*;
import com.sdd.service.MainFormServices;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@AllArgsConstructor
public class MainFormServiceImpl implements MainFormServices {

    @Autowired
    private CodeFromCategoryRepositry codeFromCategoryRepositry;

    @Autowired
    private PaymentFailedRepoRepositry paymentFailedRepoRepositry;

    @Autowired
    private PaymentSucessRepoRepositry paymentSucessRepoRepositry;

    @Autowired
    private MainFromRepositry mainFromRepositry;

    @Autowired
    private AdminCallBackRepoRepositry adminCallBackRepoRepositry;

    @Autowired
    private MainDocFromRepositry mainDocFromRepositry;

    @Autowired
    private CodeBreedTypeRepositry codeBreedTypeRepositry;

    @Autowired
    private PaymentDetailsRepositry paymentDetailsRepositry;

    @Autowired
    private CodeAddressVillageRepositry villageRepositry;

    @Autowired
    private CodeAddressSectorRepositry codeAddressSectorRepositry;

    @Autowired
    private MangerScoietyRepositry mangerScoietyRepositry;

    @Autowired
    private UserAccountRepositry userAccountRepositry;

    @Autowired
    private DoctorRepositry doctorRepositry;

    @Autowired
    private VacinationBookRepositry vacinationBookRepositry;

    @Autowired
    private AppointmentRepositry appointmentRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;


    @Override
    public ApiResponse<List<MainFormResponse>> getAllMainComplete() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatus(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "CO");
        //        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumber(currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
        //        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatus("9811011100","CO");

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);
            MainFormResponse mainFormData = new MainFormResponse();
            MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
            BeanUtils.copyProperties(data, mainFormData);

            MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
            CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());
            BeanUtils.copyProperties(docData, mainDocFormResponse);
            mainFormData.setMainDocFormResponse(mainDocFormResponse);
            mainFormData.setBreedName(brredType.getDescr());

            List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
            if (paymentInfo.size() > 0) {
                mainFormData.setTransactionData(paymentInfo.get(0));
            }

            mainFormResponses.add(mainFormData);
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });

    }

    @Override
    public ApiResponse<MainFormResponse> getAllMainCompleteRenewForm(String formId) {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
        MainFormResponse mainFormResponses = new MainFormResponse();


        MainForm getAllMainFormData = mainFromRepositry.findByFormId(formId);
        MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
        BeanUtils.copyProperties(getAllMainFormData, mainFormResponses);

        MainDocForm docData = mainDocFromRepositry.findByFormId(getAllMainFormData.getFormId());
        CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(getAllMainFormData.getBreedId().getBreedId());
        CodeFromCategory dogDetail = codeFromCategoryRepositry.findByCatIdOrderByDescrAsc(brredType.getCatId());

        if (docData != null) {
            BeanUtils.copyProperties(docData, mainDocFormResponse);
            mainDocFormResponse.setDoc_form_id(docData.getDocFormId());
        }


        mainFormResponses.setMainDocFormResponse(mainDocFormResponse);
        mainFormResponses.setBreedName(brredType.getDescr());
        mainFormResponses.setPetType(dogDetail.getDescr());

        List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(getAllMainFormData.getOrderId());
        if (paymentInfo.size() > 0) {
            mainFormResponses.setTransactionData(paymentInfo.get(0));
        }
        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<MainFormResponse>() {
        });


    }


    @Override
    public ApiResponse<List<MainFormResponse>> getAllPetPendingStatus() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatus(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "CO");
        //        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumber(currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
        //        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatus("9811011100","CO");

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {


            MainForm data = getAllMainFormData.get(i);

            if (data.getApproverState() == null || data.getApproverRemark() == null || data.getApproverState().toString().equalsIgnoreCase("IB") || data.getApproverState().toString().equalsIgnoreCase("RT") || data.getVerifierState().toString().equalsIgnoreCase("RT")
                    || data.getVerifierState() == null || data.getVerifierRemark() == null || data.getVerifierState().toString().equalsIgnoreCase("IB") || data.getVerifierState().toString().equalsIgnoreCase("OB")) {

                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);


                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                if (docData != null) {
                    BeanUtils.copyProperties(docData, mainDocFormResponse);
                    mainDocFormResponse.setDoc_form_id(docData.getDocFormId());
                } else {
                    MainDocForm mainDocFormSave = new MainDocForm();

                    mainDocFormSave.setDocFormId(HelperUtils.getDocumentId());
                    mainDocFormSave.setCreatedOn(HelperUtils.getCurrentTimeStamp());
                    mainDocFormSave.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
                    mainDocFormSave.setIsFlag(1);
                    mainDocFormSave.setFormId(data.getFormId());

                    mainDocFormSave.setUpload_dog1_url("");
                    mainDocFormSave.setUpload_dog2_url("");
                    mainDocFormSave.setUpload_dog3_url("");

                    mainDocFormSave.setUpload_id_proof("");
                    mainDocFormSave.setUpload_id_url("");
                    mainDocFormSave.setUpload_photo_url("");
                    mainDocFormSave.setUpload_sign_url("");
                    mainDocFormSave.setUpload_valid_book("");
                    MainDocForm docSaveData = mainDocFromRepositry.save(mainDocFormSave);
                    mainDocFormResponse.setDoc_form_id(docSaveData.getDocFormId());
                }
                mainFormData.setMainDocFormResponse(mainDocFormResponse);

                List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                if (paymentInfo.size() > 0) {
                    mainFormData.setTransactionData(paymentInfo.get(0));
                }

                CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());
                mainFormData.setBreedName(brredType.getDescr());


                mainFormResponses.add(mainFormData);

            }

        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });

    }

    @Override
    public ApiResponse<GetRegistrationInfoResponse> getRregistrationNoInfo(String registrationNo) {
        GetRegistrationInfoResponse getRegistrationInfoResponse = new GetRegistrationInfoResponse();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
        MainForm getMainFormData = mainFromRepositry.findByNewRegistrationNo(registrationNo);
        if (getMainFormData == null) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "No data found.");
        }

        MainDocForm docData = mainDocFromRepositry.findByFormId(getMainFormData.getFormId());

        getRegistrationInfoResponse.setCertificateNo(getMainFormData.getNewRegistrationNo());
        getRegistrationInfoResponse.setOwnerName(getMainFormData.getOwnerName());
        getRegistrationInfoResponse.setPetNAme(getMainFormData.getNickName());
        getRegistrationInfoResponse.setRabiesVaccinationDate(getMainFormData.getRabbiesDate());
        getRegistrationInfoResponse.setNextRabiesVaccinationDate(getMainFormData.getNextRabbiesDate());
        getRegistrationInfoResponse.setCertificateExpired(getMainFormData.getExpiryDate());
        getRegistrationInfoResponse.setOwnerPhotoUrl(docData.getUpload_photo_url());


        return ResponseUtils.createSuccessResponse(getRegistrationInfoResponse, new TypeReference<GetRegistrationInfoResponse>() {
        });

    }

    @Override
    public ApiResponse<MainFormResponse> createMainForm(MainFormRequest mainFormRequest) {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }

        if (mainFormRequest.getOwnerName() == null || mainFormRequest.getOwnerName().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER NAME CAN NOT BE BLANK");
        }
        if (mainFormRequest.getOwnerNumber() == null || mainFormRequest.getOwnerNumber().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER MOBILE NO CAN NOT BE BLANK");
        }
        if (mainFormRequest.getBreedId() == null || mainFormRequest.getBreedId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER BREED ID CAN NOT BE BLANK");
        }
        if (mainFormRequest.getCatId() == null || mainFormRequest.getCatId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER CAT_ID CAN NOT BE BLANK");
        }
        if (mainFormRequest.getRabbiesDate() == null || mainFormRequest.getRabbiesDate().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER RABBIESDATE CAN NOT BE BLANK");
        }
        if (mainFormRequest.getNickName() == null || mainFormRequest.getNickName().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER NIK NAME CAN NOT BE BLANK");
        }
        if (mainFormRequest.getSex() == null || mainFormRequest.getSex().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER SEX CAN NOT BE BLANK");
        }
        if (mainFormRequest.getDob() == null || mainFormRequest.getDob().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER PET DOB CAN NOT BE BLANK");
        }
        if (mainFormRequest.getAmount() == null || mainFormRequest.getAmount().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "AMOUNT CAN NOT BE BLANK");
        }
        if (mainFormRequest.getNextRabbiesDate() == null || mainFormRequest.getNextRabbiesDate().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "NEXT RABBIES DATE CAN NOT BE BLANK");
        }
        if (mainFormRequest.getCreatorId() == null || mainFormRequest.getCreatorId().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "CRAETER ID CAN NOT BE BLANK");
        }
        if (mainFormRequest.getOrderId() == null || mainFormRequest.getOrderId().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "ORDER ID CAN NOT BE BLANK");
        }
        if (mainFormRequest.getIsNeutering() == null || mainFormRequest.getIsNeutering().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "isNeutering CAN NOT BE BLANK");
        }


        MainForm mainForm = new MainForm();
        mainForm.setFormId(HelperUtils.getFormId());
        mainForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        mainForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        mainForm.setIsFlag(1);
        mainForm.setAddress(mainFormRequest.getAddress());
        mainForm.setAmount(mainFormRequest.getAmount());
        mainForm.setCreatorState("PE");
        mainForm.setDob(mainFormRequest.getDob());
        mainForm.setDogId(HelperUtils.getDogId());
        mainForm.setFormType("ON");
        mainForm.setSex(mainFormRequest.getSex());
        mainForm.setIsRenewed(0);
        mainForm.setIsUpdate("0");
        mainForm.setNextRabbiesDate(ConverterUtils.convertDateTotimeStamp(mainFormRequest.getNextRabbiesDate()));
        mainForm.setNickName(mainFormRequest.getNickName());

        mainForm.setOwnerNumber(mainFormRequest.getOwnerNumber());
        mainForm.setOwnerName(mainFormRequest.getOwnerName());
        mainForm.setRabbiesDate(ConverterUtils.convertDateTotimeStamp(mainFormRequest.getRabbiesDate()));
        mainForm.setVerifierState("IB");

        mainForm.setAddLine1(mainFormRequest.getAddLine1());
        mainForm.setAddressType(mainFormRequest.getAddressType());
        mainForm.setBlock(mainFormRequest.getBlock());
        mainForm.setFlatNo(mainFormRequest.getFlatNo());
        mainForm.setLandmark(mainFormRequest.getLandmark());
        mainForm.setExpiryDate(ConverterUtils.convertDateTotimeStamp("31/03/2024"));
        mainForm.setIsNeutering(mainFormRequest.getIsNeutering());
        mainForm.setOrderId(mainFormRequest.getOrderId());

        mainForm.setPincode(mainFormRequest.getPincode());
        mainForm.setSocietyOther(mainFormRequest.getSocietyOther());
        mainForm.setStreetNo(mainFormRequest.getStreetNo());
        mainForm.setTowerNo(mainFormRequest.getTowerNo());


        CodeAddressVillage codeAddressVillage = villageRepositry.findByVillageId(mainFormRequest.getVillageId());
        CodeBreedType codeBreedType = codeBreedTypeRepositry.findByBreedId(mainFormRequest.getBreedId());
        CodeFromCategory codeFromCategory = codeFromCategoryRepositry.findByCatIdOrderByDescrAsc(mainFormRequest.getCatId());
        MangerScoiety mangerScoiety = mangerScoietyRepositry.findByManageSocietyId(mainFormRequest.getManageSocietyId());
        CodeAddressSector codeAddressSector = codeAddressSectorRepositry.findBySectorId(mainFormRequest.getSectorId());
        UserAccount userAccount = userAccountRepositry.findByUserAccId(mainFormRequest.getCreatorId());


        mainForm.setSectorId(codeAddressSector);
        mainForm.setVillageId(codeAddressVillage);
        mainForm.setBreedId(codeBreedType);
        mainForm.setCatId(codeFromCategory);
        mainForm.setCreatorId(userAccount);
        mainForm.setManageSocietyId(mangerScoiety);


        MainForm saveData = mainFromRepositry.save(mainForm);

        MainFormResponse mainFormResponse = new MainFormResponse();
        mainFormResponse.setFormId(saveData.getFormId());


        return ResponseUtils.createSuccessResponse(mainFormResponse, new TypeReference<MainFormResponse>() {
        });

    }


    @Override
    public ApiResponse<MainFormResponse> createMainForm1(MainFormRequest mainFormRequest) {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }

        if (mainFormRequest.getOwnerName() == null || mainFormRequest.getOwnerName().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER NAME CAN NOT BE BLANK");
        }
        if (mainFormRequest.getOwnerNumber() == null || mainFormRequest.getOwnerNumber().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER MOBILE NO CAN NOT BE BLANK");
        }
        if (mainFormRequest.getBreedId() == null || mainFormRequest.getBreedId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER BREED ID CAN NOT BE BLANK");
        }
        if (mainFormRequest.getCatId() == null || mainFormRequest.getCatId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER CAT_ID CAN NOT BE BLANK");
        }
//        if (mainFormRequest.getRabbiesDate() == null || mainFormRequest.getRabbiesDate().toString().isEmpty()) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER RABBIESDATE CAN NOT BE BLANK");
//        }
        if (mainFormRequest.getNickName() == null || mainFormRequest.getNickName().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER NIK NAME CAN NOT BE BLANK");
        }
        if (mainFormRequest.getSex() == null || mainFormRequest.getSex().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER SEX CAN NOT BE BLANK");
        }
        if (mainFormRequest.getDob() == null || mainFormRequest.getDob().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER PET DOB CAN NOT BE BLANK");
        }
        if (mainFormRequest.getAmount() == null || mainFormRequest.getAmount().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "AMOUNT CAN NOT BE BLANK");
        }
//        if (mainFormRequest.getNextRabbiesDate() == null || mainFormRequest.getNextRabbiesDate().toString().isEmpty()) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "NEXT RABIES DATE CAN NOT BE BLANK");
//        }
        if (mainFormRequest.getCreatorId() == null || mainFormRequest.getCreatorId().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "CRATER ID CAN NOT BE BLANK");
        }
//        if (mainFormRequest.getOrderId() == null || mainFormRequest.getOrderId().toString().isEmpty()) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "ORDER ID CAN NOT BE BLANK");
//        }
//        if (mainFormRequest.getIsNeutering() == null || mainFormRequest.getIsNeutering().toString().isEmpty()) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "isNeutering CAN NOT BE BLANK");
//        }


        List<MainForm> mainFormData = mainFromRepositry.findByOwnerNumber(mainFormRequest.getOwnerNumber());
        for (Integer i = 0; i < mainFormData.size(); i++) {

            MainForm findDuplicateData = mainFormData.get(i);

            if (findDuplicateData.getBreedId().getBreedId().equalsIgnoreCase(mainFormRequest.getBreedId())
                    && findDuplicateData.getNickName().equalsIgnoreCase(mainFormRequest.getNickName())
                    && findDuplicateData.getOwnerNumber().equalsIgnoreCase(mainFormRequest.getOwnerNumber())
                    && findDuplicateData.getSex().equalsIgnoreCase(mainFormRequest.getSex())) {

                if (findDuplicateData.getPaymentStatus() == null) {
                    throw new SDDException(HttpStatus.ALREADY_REPORTED.value(), "THIS DOG ALREADY REGISTER. CHECK IN NEW REGISTRATION/RENEWAL OPTION > DOG LIST");
                } else {
                    throw new SDDException(HttpStatus.ALREADY_REPORTED.value(), "THIS DOG ALREADY REGISTER. CHECK IN REGISTRATION STATUS/VERIFY PETS");
                }
            }
        }


//        List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnId(mainFormRequest.getOrderId());
//        if (paymentDetails.size() > 0) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "ORDER ID ALREADY EXIST" + mainFormRequest.getOrderId());
//        }


        MainForm mainForm = new MainForm();
        CodeBreedType codeBreedType = codeBreedTypeRepositry.findByBreedId(mainFormRequest.getBreedId());


        if (codeBreedType.getBreedId().equalsIgnoreCase("breed_Id_166")) {
            mainForm.setAmount("0");

        } else {
            mainForm.setAmount("500");
        }


        String orderId = HelperUtils.getOrderId();
        List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnId(orderId);
        if (paymentDetails.size() > 0) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "ORDER ID ALREADY EXIST" + mainFormRequest.getOrderId());
        }

        mainForm.setFormId(HelperUtils.getFormId());
        mainForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        mainForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        mainForm.setIsFlag(1);
        mainForm.setAddress(mainFormRequest.getAddress());

        mainForm.setCreatorState("PE");
        mainForm.setDob(mainFormRequest.getDob());
        mainForm.setDogId(HelperUtils.getDogId());
        mainForm.setFormType("ON");
        mainForm.setSex(mainFormRequest.getSex());
        mainForm.setIsRenewed(0);
        mainForm.setIsUpdate("0");
        mainForm.setNickName(mainFormRequest.getNickName());
        mainForm.setOwnerNumber(mainFormRequest.getOwnerNumber());
        mainForm.setOwnerName(mainFormRequest.getOwnerName());

        if((mainFormRequest.getRabbiesDate().equalsIgnoreCase("null")|| mainFormRequest.getRabbiesDate().equalsIgnoreCase("please select Date"))
                && (mainFormRequest.getNextRabbiesDate().equalsIgnoreCase("null")|| mainFormRequest.getNextRabbiesDate().equalsIgnoreCase("please select Date"))){
            mainForm.setRabbiesDate(null);
            mainForm.setNextRabbiesDate(null);
        }else{
            mainForm.setRabbiesDate(ConverterUtils.convertDateTotimeStamp(mainFormRequest.getRabbiesDate()));
            mainForm.setNextRabbiesDate(ConverterUtils.convertDateTotimeStamp(mainFormRequest.getNextRabbiesDate()));
        }

        mainForm.setVerifierState("IB");

        mainForm.setAddLine1(mainFormRequest.getAddLine1());
        mainForm.setAddressType(mainFormRequest.getAddressType());
        mainForm.setBlock(mainFormRequest.getBlock());
        mainForm.setFlatNo(mainFormRequest.getFlatNo());
        mainForm.setLandmark(mainFormRequest.getLandmark());
        mainForm.setExpiryDate(ConverterUtils.getNextYearDate());

        mainForm.setOrderId(orderId);
        mainForm.setTxnId(orderId);
        mainForm.setIsNeutering(mainFormRequest.getIsNeutering());

        mainForm.setPincode(mainFormRequest.getPincode());
        mainForm.setSocietyOther(mainFormRequest.getSocietyOther());
        mainForm.setStreetNo(mainFormRequest.getStreetNo());
        mainForm.setTowerNo(mainFormRequest.getTowerNo());


        mainForm.setIsPendingList("0");
        mainForm.setIsUpdate("0");


        CodeAddressVillage codeAddressVillage = villageRepositry.findByVillageId(mainFormRequest.getVillageId());
        CodeFromCategory codeFromCategory = codeFromCategoryRepositry.findByCatIdOrderByDescrAsc(mainFormRequest.getCatId());
        MangerScoiety mangerScoiety = mangerScoietyRepositry.findByManageSocietyId(mainFormRequest.getManageSocietyId());
        CodeAddressSector codeAddressSector = codeAddressSectorRepositry.findBySectorId(mainFormRequest.getSectorId());
        UserAccount userAccount = userAccountRepositry.findByUserAccId(mainFormRequest.getCreatorId());


        mainForm.setSectorId(codeAddressSector);
        mainForm.setVillageId(codeAddressVillage);
        mainForm.setBreedId(codeBreedType);
        mainForm.setCatId(codeFromCategory);
        mainForm.setCreatorId(userAccount);
        mainForm.setManageSocietyId(mangerScoiety);


        MainForm saveData = mainFromRepositry.save(mainForm);

        MainFormResponse mainFormResponse = new MainFormResponse();
        mainFormResponse.setFormId(saveData.getFormId());
        mainFormResponse.setOrderId(saveData.getOrderId());


        return ResponseUtils.createSuccessResponse(mainFormResponse, new TypeReference<MainFormResponse>() {
        });

    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod1(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, String formId) throws IOException {
        UplaodMainFormDocumentsResponse uplaodDocuments = new UplaodMainFormDocumentsResponse();
        try {
            String mainFilePath = "data/mcpets/formSupprotDoc/" + formId + "/";   //live
//            String mainFilePath = "/Users/manjuyadav/Desktop/Dee/" + formId + "/";  // local
//            //String mainFilePath = "/usr/local/tomcat/webapps/Users/manjuyadav/Desktop/Dee/" + formId + "/";


            if (pet1 == null || pet1.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo1 can not be blank.");
            }
            if (pet2 == null || pet2.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo2 can not be blank.");
            }

            if (pet3 == null || pet3.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo3 can not be blank.");
            }

            if (file4 == null || file4.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo1 can not be blank.");
            }
            if (file5 == null || file5.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo2 can not be blank.");
            }
            if (file6 == null || file6.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
            }
            if (file7 == null || file7.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo4 can not be blank.");
            }
//            if (file8 == null || file8.isEmpty()) {
//                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
//            }
            if (formId == null || formId.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form ID can not be blank.");
            }


            MainForm getMainFormData = mainFromRepositry.findByFormId(formId);
            if (getMainFormData == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/15/" + formId);
            }


            String fileExtension1 = ConverterUtils.getFileExtension(pet1);
            String fileExtension2 = ConverterUtils.getFileExtension(pet2);
            String fileExtension3 = ConverterUtils.getFileExtension(pet3);
            String fileExtension4 = ConverterUtils.getFileExtension(file4);
            String fileExtension5 = ConverterUtils.getFileExtension(file5);
            String fileExtension6 = ConverterUtils.getFileExtension(file6);
            String fileExtension7 = ConverterUtils.getFileExtension(file7);
            String fileExtension8 ="";
            if(file8 != null){
                fileExtension8 = ConverterUtils.getFileExtension(file8);
            }

            if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")
                    || fileExtension2.equalsIgnoreCase(".jpeg") || fileExtension2.equalsIgnoreCase(".png") || fileExtension2.equalsIgnoreCase(".jpg")
                    || fileExtension3.equalsIgnoreCase(".jpeg") || fileExtension3.equalsIgnoreCase(".png") || fileExtension3.equalsIgnoreCase(".jpg")
                    || fileExtension4.equalsIgnoreCase(".jpeg") || fileExtension4.equalsIgnoreCase(".png") || fileExtension4.equalsIgnoreCase(".jpg")
                    || fileExtension5.equalsIgnoreCase(".jpeg") || fileExtension5.equalsIgnoreCase(".png") || fileExtension5.equalsIgnoreCase(".jpg")
                    || fileExtension6.equalsIgnoreCase(".jpeg") || fileExtension6.equalsIgnoreCase(".png") || fileExtension6.equalsIgnoreCase(".jpg")
                    || fileExtension7.equalsIgnoreCase(".jpeg") || fileExtension7.equalsIgnoreCase(".png") || fileExtension7.equalsIgnoreCase(".jpg")
                //|| fileExtension8.equalsIgnoreCase(".jpeg") || fileExtension8.equalsIgnoreCase(".png") || fileExtension8.equalsIgnoreCase(".jpg")
            ) {

            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
            }
            String filename1 = ConverterUtils.getRandomString("dog1");
            String filename2 = ConverterUtils.getRandomString("dogw");
            String filename3 = ConverterUtils.getRandomString("dog2");
            String filename4 = ConverterUtils.getRandomString("photoId");
            String filename5 = ConverterUtils.getRandomString("ownerPhoto");
            String filename6 = ConverterUtils.getRandomString("sign");
            String filename7 = ConverterUtils.getRandomString("addressProof");
            String filename8 = "";
            if(fileExtension8 != null && !(fileExtension8.isEmpty())){
                filename8 = ConverterUtils.getRandomString("vaccination");
            }


            File targetFile1 = ConverterUtils.getTargetFile(fileExtension1, filename1, mainFilePath + "dog/");
            File targetFile2 = ConverterUtils.getTargetFile(fileExtension2, filename2, mainFilePath + "dog/");
            File targetFile3 = ConverterUtils.getTargetFile(fileExtension3, filename3, mainFilePath + "dog/");
            File targetFile4 = ConverterUtils.getTargetFile(fileExtension4, filename4, mainFilePath + "uploadDocs/");
            File targetFile5 = ConverterUtils.getTargetFile(fileExtension5, filename5, mainFilePath + "uploadDocs/");
            File targetFile6 = ConverterUtils.getTargetFile(fileExtension6, filename6, mainFilePath + "uploadDocs/");
            File targetFile7 = ConverterUtils.getTargetFile(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            //File targetFile8 = ConverterUtils.getTargetFile(fileExtension8, filename8, mainFilePath + "uploadDocs/");
            File targetFile8 = null;
            if(filename8 != null && !(filename8.isEmpty())) {
                targetFile8 = ConverterUtils.getTargetFile(fileExtension8, filename8, mainFilePath + "uploadDocs/");
            }


            File mainPath1 = ConverterUtils.getComplaintPathOnly(fileExtension1, filename1, mainFilePath + "dog/");
            File mainPath2 = ConverterUtils.getComplaintPathOnly(fileExtension2, filename2, mainFilePath + "dog/");
            File mainPath3 = ConverterUtils.getComplaintPathOnly(fileExtension3, filename3, mainFilePath + "dog/");
            File mainPath4 = ConverterUtils.getComplaintPathOnly(fileExtension4, filename4, mainFilePath + "uploadDocs/");
            File mainPath5 = ConverterUtils.getComplaintPathOnly(fileExtension5, filename5, mainFilePath + "uploadDocs/");
            File mainPath6 = ConverterUtils.getComplaintPathOnly(fileExtension6, filename6, mainFilePath + "uploadDocs/");
            File mainPath7 = ConverterUtils.getComplaintPathOnly(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            //File mainPath8 = ConverterUtils.getComplaintPathOnly(fileExtension8, filename8, mainFilePath + "uploadDocs/");
            File mainPath8 = null;
            if(filename8 != null && !(filename8.isEmpty())){
                mainPath8 = ConverterUtils.getComplaintPathOnly(fileExtension8, filename8, mainFilePath + "uploadDocs/");
            }



            Path path1 = Paths.get(targetFile1.toString());
            Path path2 = Paths.get(targetFile2.toString());
            Path path3 = Paths.get(targetFile3.toString());
            Path path4 = Paths.get(targetFile4.toString());
            Path path5 = Paths.get(targetFile5.toString());
            Path path6 = Paths.get(targetFile6.toString());
            Path path7 = Paths.get(targetFile7.toString());
            //Path path8 = Paths.get(targetFile8.toString());

            Path path8=null;
            if(targetFile8!=null && !(targetFile8.exists())){
                path8 = Paths.get(targetFile8.toString());
            }
            InputStream in1 = new ByteArrayInputStream(pet1.getBytes());
            InputStream in2 = new ByteArrayInputStream(pet2.getBytes());
            InputStream in3 = new ByteArrayInputStream(pet3.getBytes());
            InputStream in4 = new ByteArrayInputStream(file4.getBytes());
            InputStream in5 = new ByteArrayInputStream(file5.getBytes());
            InputStream in6 = new ByteArrayInputStream(file6.getBytes());
            InputStream in7 = new ByteArrayInputStream(file7.getBytes());
            //InputStream in8 = new ByteArrayInputStream(file8.getBytes());

            try {
                System.out.println(
                        "Number of bytes copied1: "
                                + Files.copy(
                                in1, path1.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(
                        "Number of bytes copied2: "
                                + Files.copy(
                                in2, path2.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied3: "
                                + Files.copy(
                                in3, path3.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied4: "
                                + Files.copy(
                                in4, path4.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied5: "
                                + Files.copy(
                                in5, path5.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied6: "
                                + Files.copy(
                                in6, path6.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied7: "
                                + Files.copy(
                                in7, path7.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            InputStream in8=null;
            if(file8!=null && !(file8.isEmpty())){
                in8 = new ByteArrayInputStream(file8.getBytes());
                try {
                    System.out.println("Number of bytes copied8: " + Files.copy(in8, path8.toAbsolutePath(),
                            StandardCopyOption.REPLACE_EXISTING));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            MainDocForm mainDocForm = new MainDocForm();
            mainDocForm.setDocFormId(HelperUtils.getDocumentId());
            mainDocForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setIsFlag(1);
            mainDocForm.setFormId(formId);


            mainDocForm.setUpload_dog1_url(mainPath1.toString());
            mainDocForm.setUpload_dog2_url(mainPath2.toString());
            mainDocForm.setUpload_dog3_url(mainPath3.toString());

            mainDocForm.setUpload_id_proof(mainPath7.toString());
            mainDocForm.setUpload_id_url(mainPath4.toString());
            mainDocForm.setUpload_photo_url(mainPath5.toString());
            mainDocForm.setUpload_sign_url(mainPath6.toString());

            if(filename8 != null&& !filename8.equalsIgnoreCase("")){
                mainDocForm.setUpload_valid_book(mainPath8.toString());
            }

            uplaodDocuments.setDog1(path1.toString());
            uplaodDocuments.setOwnerWithPet(path2.toString());
            uplaodDocuments.setDog2(path3.toString());
            uplaodDocuments.setUploadPhotoId(path4.toString());
            uplaodDocuments.setOwnerPhoto(path5.toString());
            uplaodDocuments.setSign(path6.toString());
            uplaodDocuments.setAddressPrrof(path7.toString());
            if(path8 !=null&& !path8.endsWith("")){
                uplaodDocuments.setVacinationbook(path8.toString());
            }



            try {
                MainForm mainForm = mainFromRepositry.findByFormId(formId);
                CodeBreedType codeBreedType = codeBreedTypeRepositry.findByBreedId(mainForm.getBreedId().getBreedId());
                List<MainForm> mainFormListData = mainFromRepositry.findByOwnerNumberAndPaymentStatusAndBreedId(mainForm.getOwnerNumber(), "CO", codeBreedType);
                if (mainFormListData.size() > 10) {
                    uplaodDocuments.setAmount("500");
                } else if (codeBreedType.getBreedId().equalsIgnoreCase("breed_Id_166")) {
                    uplaodDocuments.setAmount("0");

                    if (getMainFormData == null) {
                        throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/16/" + formId);
                    }

                    mainForm.setPaymentStatus("CO");
                    mainForm.setIsPendingList("1");
                    mainForm.setIsUpdate("1");
                    mainForm.setTxnId(mainForm.getOrderId());
                    mainFromRepositry.save(mainForm);

                    PaymentDetails paymentDetails = new PaymentDetails();
                    paymentDetails.setPaymentId(HelperUtils.getPaymentId());
                    paymentDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
                    paymentDetails.setIsFlag(1);
                    paymentDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
                    paymentDetails.setAmount(mainForm.getAmount());
                    paymentDetails.setName(mainForm.getOwnerName());
                    paymentDetails.setPaymentDate(HelperUtils.getCurrentTimeStamp());
                    paymentDetails.setPaymentStatus("CO");
                    paymentDetails.setPhone(mainForm.getOwnerNumber());
                    paymentDetails.setProductInfo("product_Info");
                    paymentDetails.setTxnId(mainForm.getOrderId());
                    paymentDetails.setEmail("");

                    paymentDetailsRepositry.save(paymentDetails);

                } else {
                    uplaodDocuments.setAmount("500");
                }

                MainDocForm saveData = mainDocFromRepositry.save(mainDocForm);

                uplaodDocuments.setDocId(saveData.getDocFormId());
            } catch (Exception e) {

            }


            uplaodDocuments.setMeassge("File upload successfully");

            return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        uplaodDocuments.setMeassge("File upload successfully");
        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod1(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, String formId, String docId) throws IOException {
        UplaodMainFormDocumentsResponse uplaodDocuments = new UplaodMainFormDocumentsResponse();

        String mainFilePath = "data/mcpets/formSupprotDoc/" + formId + "/";

        if (pet1 == null || pet1.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo1 can not be blank.");
        }
        if (docId == null || docId.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Document Id can not be blank.");
        }
        if (pet2 == null || pet2.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo2 can not be blank.");
        }

        if (pet3 == null || pet3.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo3 can not be blank.");
        }

        if (file4 == null || file4.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo1 can not be blank.");
        }
        if (file5 == null || file5.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo2 can not be blank.");
        }
        if (file6 == null || file6.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
        }
        if (file7 == null || file7.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo4 can not be blank.");
        }
        if (file8 == null || file8.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
        }
        if (formId == null || formId.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form ID can not be blank.");
        }
        try {

            MainForm getMainFormData = mainFromRepositry.findByFormId(formId);
            if (getMainFormData == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/11/" + formId);
            }


            String fileExtension1 = ConverterUtils.getFileExtension(pet1);
            String fileExtension2 = ConverterUtils.getFileExtension(pet2);
            String fileExtension3 = ConverterUtils.getFileExtension(pet3);
            String fileExtension4 = ConverterUtils.getFileExtension(file4);
            String fileExtension5 = ConverterUtils.getFileExtension(file5);
            String fileExtension6 = ConverterUtils.getFileExtension(file6);
            String fileExtension7 = ConverterUtils.getFileExtension(file7);
            String fileExtension8 = ConverterUtils.getFileExtension(file8);


            if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")
                    || fileExtension2.equalsIgnoreCase(".jpeg") || fileExtension2.equalsIgnoreCase(".png") || fileExtension2.equalsIgnoreCase(".jpg")
                    || fileExtension3.equalsIgnoreCase(".jpeg") || fileExtension3.equalsIgnoreCase(".png") || fileExtension3.equalsIgnoreCase(".jpg")
                    || fileExtension4.equalsIgnoreCase(".jpeg") || fileExtension4.equalsIgnoreCase(".png") || fileExtension4.equalsIgnoreCase(".jpg")
                    || fileExtension5.equalsIgnoreCase(".jpeg") || fileExtension5.equalsIgnoreCase(".png") || fileExtension5.equalsIgnoreCase(".jpg")
                    || fileExtension6.equalsIgnoreCase(".jpeg") || fileExtension6.equalsIgnoreCase(".png") || fileExtension6.equalsIgnoreCase(".jpg")
                    || fileExtension7.equalsIgnoreCase(".jpeg") || fileExtension7.equalsIgnoreCase(".png") || fileExtension7.equalsIgnoreCase(".jpg")
                    || fileExtension8.equalsIgnoreCase(".jpeg") || fileExtension8.equalsIgnoreCase(".png") || fileExtension8.equalsIgnoreCase(".jpg")
            ) {

            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
            }

            String filename1 = ConverterUtils.getRandomString("dog1");
            String filename2 = ConverterUtils.getRandomString("dogw");
            String filename3 = ConverterUtils.getRandomString("dog2");
            String filename4 = ConverterUtils.getRandomString("photoId");
            String filename5 = ConverterUtils.getRandomString("ownerPhoto");
            String filename6 = ConverterUtils.getRandomString("sign");
            String filename7 = ConverterUtils.getRandomString("addressProof");
            String filename8 = ConverterUtils.getRandomString("vaccination");


            File targetFile1 = ConverterUtils.getTargetFile(fileExtension1, filename1, mainFilePath + "dog/");
            File targetFile2 = ConverterUtils.getTargetFile(fileExtension2, filename2, mainFilePath + "dog/");
            File targetFile3 = ConverterUtils.getTargetFile(fileExtension3, filename3, mainFilePath + "dog/");
            File targetFile4 = ConverterUtils.getTargetFile(fileExtension4, filename4, mainFilePath + "uploadDocs/");
            File targetFile5 = ConverterUtils.getTargetFile(fileExtension5, filename5, mainFilePath + "uploadDocs/");
            File targetFile6 = ConverterUtils.getTargetFile(fileExtension6, filename6, mainFilePath + "uploadDocs/");
            File targetFile7 = ConverterUtils.getTargetFile(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            File targetFile8 = ConverterUtils.getTargetFile(fileExtension8, filename8, mainFilePath + "uploadDocs/");


            File mainPath1 = ConverterUtils.getComplaintPathOnly(fileExtension1, filename1, mainFilePath + "dog/");
            File mainPath2 = ConverterUtils.getComplaintPathOnly(fileExtension2, filename2, mainFilePath + "dog/");
            File mainPath3 = ConverterUtils.getComplaintPathOnly(fileExtension3, filename3, mainFilePath + "dog/");
            File mainPath4 = ConverterUtils.getComplaintPathOnly(fileExtension4, filename4, mainFilePath + "uploadDocs/");
            File mainPath5 = ConverterUtils.getComplaintPathOnly(fileExtension5, filename5, mainFilePath + "uploadDocs/");
            File mainPath6 = ConverterUtils.getComplaintPathOnly(fileExtension6, filename6, mainFilePath + "uploadDocs/");
            File mainPath7 = ConverterUtils.getComplaintPathOnly(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            File mainPath8 = ConverterUtils.getComplaintPathOnly(fileExtension8, filename8, mainFilePath + "uploadDocs/");


            Path path1 = Paths.get(targetFile1.toString());
            Path path2 = Paths.get(targetFile2.toString());
            Path path3 = Paths.get(targetFile3.toString());
            Path path4 = Paths.get(targetFile4.toString());
            Path path5 = Paths.get(targetFile5.toString());
            Path path6 = Paths.get(targetFile6.toString());
            Path path7 = Paths.get(targetFile7.toString());
            Path path8 = Paths.get(targetFile8.toString());


            InputStream in1 = new ByteArrayInputStream(pet1.getBytes());
            InputStream in2 = new ByteArrayInputStream(pet2.getBytes());
            InputStream in3 = new ByteArrayInputStream(pet3.getBytes());
            InputStream in4 = new ByteArrayInputStream(file4.getBytes());
            InputStream in5 = new ByteArrayInputStream(file5.getBytes());
            InputStream in6 = new ByteArrayInputStream(file6.getBytes());
            InputStream in7 = new ByteArrayInputStream(file7.getBytes());
            InputStream in8 = new ByteArrayInputStream(file8.getBytes());

            try {
                System.out.println(
                        "Number of bytes copied1: "
                                + Files.copy(
                                in1, path1.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(
                        "Number of bytes copied2: "
                                + Files.copy(
                                in2, path2.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied3: "
                                + Files.copy(
                                in3, path3.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied4: "
                                + Files.copy(
                                in4, path4.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied5: "
                                + Files.copy(
                                in5, path5.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied6: "
                                + Files.copy(
                                in6, path6.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied7: "
                                + Files.copy(
                                in7, path7.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                System.out.println(
                        "Number of bytes copied8: "
                                + Files.copy(
                                in8, path8.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            MainDocForm mainDocForm = new MainDocForm();
            mainDocForm.setDocFormId(docId);
            mainDocForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setIsFlag(1);
            mainDocForm.setFormId(formId);


            mainDocForm.setUpload_dog1_url(mainPath1.toString());
            mainDocForm.setUpload_dog2_url(mainPath2.toString());
            mainDocForm.setUpload_dog3_url(mainPath3.toString());

            mainDocForm.setUpload_id_proof(mainPath7.toString());
            mainDocForm.setUpload_id_url(mainPath4.toString());
            mainDocForm.setUpload_photo_url(mainPath5.toString());
            mainDocForm.setUpload_sign_url(mainPath6.toString());
            mainDocForm.setUpload_valid_book(mainPath8.toString());


            uplaodDocuments.setDog1(path1.toString());
            uplaodDocuments.setOwnerWithPet(path2.toString());
            uplaodDocuments.setDog2(path3.toString());
            uplaodDocuments.setUploadPhotoId(path4.toString());
            uplaodDocuments.setOwnerPhoto(path5.toString());
            uplaodDocuments.setSign(path6.toString());
            uplaodDocuments.setAddressPrrof(path7.toString());
            uplaodDocuments.setVacinationbook(path8.toString());


            try {
                MainForm mainForm = mainFromRepositry.findByFormId(formId);


                mainForm.setVerifierState("IB");
                mainForm.setVerifierId(null);
                mainForm.setApproverState(null);
                mainForm.setApproverRemark(null);
                mainFromRepositry.save(mainForm);
                MainDocForm saveData = mainDocFromRepositry.save(mainDocForm);
                uplaodDocuments.setDocId(saveData.getDocFormId());
            } catch (Exception e) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Error Occurred.");
            }
            uplaodDocuments.setMeassge("File upload successfully");

            return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        uplaodDocuments.setMeassge("File upload successfully");
        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }


    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, String formId) throws IOException {
        UplaodMainFormDocumentsResponse uplaodDocuments = new UplaodMainFormDocumentsResponse();
        try {
            String mainFilePath = "data/mcpets/formSupprotDoc/" + formId + "/";

            if (pet1 == null || pet1.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo1 can not be blank.");
            }
            if (pet2 == null || pet2.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo2 can not be blank.");
            }

            if (pet3 == null || pet3.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo3 can not be blank.");
            }

            if (file4 == null || file4.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo1 can not be blank.");
            }
            if (file5 == null || file5.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo2 can not be blank.");
            }
            if (file6 == null || file6.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
            }
            if (file7 == null || file7.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo4 can not be blank.");
            }
            if (file8 == null || file8.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
            }
            if (formId == null || formId.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form ID can not be blank.");
            }


            MainForm getMainFormData = mainFromRepositry.findByFormId(formId);
            if (getMainFormData == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/12/" + formId);
            }


            String fileExtension1 = ConverterUtils.getFileExtension(pet1);
            String fileExtension2 = ConverterUtils.getFileExtension(pet2);
            String fileExtension3 = ConverterUtils.getFileExtension(pet3);
            String fileExtension4 = ConverterUtils.getFileExtension(file4);
            String fileExtension5 = ConverterUtils.getFileExtension(file5);
            String fileExtension6 = ConverterUtils.getFileExtension(file6);
            String fileExtension7 = ConverterUtils.getFileExtension(file7);
            String fileExtension8 = ConverterUtils.getFileExtension(file8);


            if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")
                    || fileExtension2.equalsIgnoreCase(".jpeg") || fileExtension2.equalsIgnoreCase(".png") || fileExtension2.equalsIgnoreCase(".jpg")
                    || fileExtension3.equalsIgnoreCase(".jpeg") || fileExtension3.equalsIgnoreCase(".png") || fileExtension3.equalsIgnoreCase(".jpg")
                    || fileExtension4.equalsIgnoreCase(".jpeg") || fileExtension4.equalsIgnoreCase(".png") || fileExtension4.equalsIgnoreCase(".jpg")
                    || fileExtension5.equalsIgnoreCase(".jpeg") || fileExtension5.equalsIgnoreCase(".png") || fileExtension5.equalsIgnoreCase(".jpg")
                    || fileExtension6.equalsIgnoreCase(".jpeg") || fileExtension6.equalsIgnoreCase(".png") || fileExtension6.equalsIgnoreCase(".jpg")
                    || fileExtension7.equalsIgnoreCase(".jpeg") || fileExtension7.equalsIgnoreCase(".png") || fileExtension7.equalsIgnoreCase(".jpg")
                    || fileExtension8.equalsIgnoreCase(".jpeg") || fileExtension8.equalsIgnoreCase(".png") || fileExtension8.equalsIgnoreCase(".jpg")
            ) {

            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
            }

            String filename1 = ConverterUtils.getRandomString("dog1");
            String filename2 = ConverterUtils.getRandomString("dogw");
            String filename3 = ConverterUtils.getRandomString("dog2");
            String filename4 = ConverterUtils.getRandomString("photoId");
            String filename5 = ConverterUtils.getRandomString("ownerPhoto");
            String filename6 = ConverterUtils.getRandomString("sign");
            String filename7 = ConverterUtils.getRandomString("addressProof");
            String filename8 = ConverterUtils.getRandomString("vaccination");


            File targetFile1 = ConverterUtils.getTargetFile(fileExtension1, filename1, mainFilePath + "dog/");
            File targetFile2 = ConverterUtils.getTargetFile(fileExtension2, filename2, mainFilePath + "dog/");
            File targetFile3 = ConverterUtils.getTargetFile(fileExtension3, filename3, mainFilePath + "dog/");
            File targetFile4 = ConverterUtils.getTargetFile(fileExtension4, filename4, mainFilePath + "uploadDocs/");
            File targetFile5 = ConverterUtils.getTargetFile(fileExtension5, filename5, mainFilePath + "uploadDocs/");
            File targetFile6 = ConverterUtils.getTargetFile(fileExtension6, filename6, mainFilePath + "uploadDocs/");
            File targetFile7 = ConverterUtils.getTargetFile(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            File targetFile8 = ConverterUtils.getTargetFile(fileExtension8, filename8, mainFilePath + "uploadDocs/");


            File mainPath1 = ConverterUtils.getComplaintPathOnly(fileExtension1, filename1, mainFilePath + "dog/");
            File mainPath2 = ConverterUtils.getComplaintPathOnly(fileExtension2, filename2, mainFilePath + "dog/");
            File mainPath3 = ConverterUtils.getComplaintPathOnly(fileExtension3, filename3, mainFilePath + "dog/");
            File mainPath4 = ConverterUtils.getComplaintPathOnly(fileExtension4, filename4, mainFilePath + "uploadDocs/");
            File mainPath5 = ConverterUtils.getComplaintPathOnly(fileExtension5, filename5, mainFilePath + "uploadDocs/");
            File mainPath6 = ConverterUtils.getComplaintPathOnly(fileExtension6, filename6, mainFilePath + "uploadDocs/");
            File mainPath7 = ConverterUtils.getComplaintPathOnly(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            File mainPath8 = ConverterUtils.getComplaintPathOnly(fileExtension8, filename8, mainFilePath + "uploadDocs/");


            Path path1 = Paths.get(targetFile1.toString());
            Path path2 = Paths.get(targetFile2.toString());
            Path path3 = Paths.get(targetFile3.toString());
            Path path4 = Paths.get(targetFile4.toString());
            Path path5 = Paths.get(targetFile5.toString());
            Path path6 = Paths.get(targetFile6.toString());
            Path path7 = Paths.get(targetFile7.toString());
            Path path8 = Paths.get(targetFile8.toString());


            InputStream in1 = new ByteArrayInputStream(pet1.getBytes());
            InputStream in2 = new ByteArrayInputStream(pet2.getBytes());
            InputStream in3 = new ByteArrayInputStream(pet3.getBytes());
            InputStream in4 = new ByteArrayInputStream(file4.getBytes());
            InputStream in5 = new ByteArrayInputStream(file5.getBytes());
            InputStream in6 = new ByteArrayInputStream(file6.getBytes());
            InputStream in7 = new ByteArrayInputStream(file7.getBytes());
            InputStream in8 = new ByteArrayInputStream(file8.getBytes());

            try {
                System.out.println(
                        "Number of bytes copied1: "
                                + Files.copy(
                                in1, path1.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(
                        "Number of bytes copied2: "
                                + Files.copy(
                                in2, path2.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied3: "
                                + Files.copy(
                                in3, path3.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied4: "
                                + Files.copy(
                                in4, path4.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied5: "
                                + Files.copy(
                                in5, path5.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied6: "
                                + Files.copy(
                                in6, path6.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied7: "
                                + Files.copy(
                                in7, path7.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                System.out.println(
                        "Number of bytes copied8: "
                                + Files.copy(
                                in8, path8.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            MainDocForm mainDocForm = new MainDocForm();
            mainDocForm.setDocFormId(HelperUtils.getDocumentId());
            mainDocForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setIsFlag(1);
            mainDocForm.setFormId(formId);


            mainDocForm.setUpload_dog1_url(mainPath1.toString());
            mainDocForm.setUpload_dog2_url(mainPath2.toString());
            mainDocForm.setUpload_dog3_url(mainPath3.toString());

            mainDocForm.setUpload_id_proof(mainPath7.toString());
            mainDocForm.setUpload_id_url(mainPath4.toString());
            mainDocForm.setUpload_photo_url(mainPath5.toString());
            mainDocForm.setUpload_sign_url(mainPath6.toString());
            mainDocForm.setUpload_valid_book(mainPath8.toString());
            //
            //            if (!(file8 == null) || !(file8.isEmpty())) {
            //                String fileExtension9 = ConverterUtils.getFileExtension(file9);
            //                if (fileExtension9.equalsIgnoreCase(".jpeg") || fileExtension9.equalsIgnoreCase(".png") || fileExtension9.equalsIgnoreCase(".jpg")) {
            //
            //                } else {
            //                    throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
            //                }
            //
            //                String filename9 = ConverterUtils.getRandomString(file9);
            //                File targetFile9 = ConverterUtils.getTargetFile(fileExtension9, filename9, mainFilePath + "uploadDocs/");
            //                File mainPath9 = ConverterUtils.getComplaintPathOnly(fileExtension9, filename9, mainFilePath + "uploadDocs/");
            //                Path path9 = Paths.get(targetFile9.toString());
            //                InputStream in9 = new ByteArrayInputStream(file9.getBytes());
            //
            //                try {
            //
            //                    System.out.println(
            //                            "Number of bytes copied9: "
            //                                    + Files.copy(
            //                                    in9, path9.toAbsolutePath(),
            //                                    StandardCopyOption.REPLACE_EXISTING));
            //                } catch (IOException e) {
            //                    e.printStackTrace();
            //                }
            //                mainDocForm.setUpload_valid_book2(mainPath9.toString());
            //
            //            }


            uplaodDocuments.setDog1(path1.toString());
            uplaodDocuments.setOwnerWithPet(path2.toString());
            uplaodDocuments.setDog2(path3.toString());
            uplaodDocuments.setUploadPhotoId(path4.toString());
            uplaodDocuments.setOwnerPhoto(path5.toString());
            uplaodDocuments.setSign(path6.toString());
            uplaodDocuments.setAddressPrrof(path7.toString());
            uplaodDocuments.setVacinationbook(path8.toString());


            try {
                MainDocForm saveData = mainDocFromRepositry.save(mainDocForm);
                uplaodDocuments.setDocId(saveData.getDocFormId());
            } catch (Exception e) {

            }


            uplaodDocuments.setMeassge("File upload successfully");
            uplaodDocuments.setAmount("500");


            return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        uplaodDocuments.setMeassge("File upload successfully");
        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }

    @Override
    public ApiResponse<UpdatePaymentResponse> updatePaymentInfo(UpdatePaymentRequest mainFormRequest) {

        MainForm getMainFormData = mainFromRepositry.findByFormId(mainFormRequest.getFormId());
        if (getMainFormData == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/13/" + mainFormRequest.getFormId());
        }

        if (getMainFormData.getOrderId() == null || getMainFormData.getOrderId().isEmpty()) {
            getMainFormData.setOrderId(mainFormRequest.getOrderId());
        }

        getMainFormData.setPaymentStatus("CO");
        getMainFormData.setIsPendingList("1");
        getMainFormData.setIsUpdate("1");
        getMainFormData.setTransactionData(mainFormRequest.getTxnData());
        getMainFormData.setTxnId(mainFormRequest.getOrderId());
        MainForm data = mainFromRepositry.save(getMainFormData);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentId(HelperUtils.getPaymentId());
        paymentDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        paymentDetails.setIsFlag(1);
        paymentDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        paymentDetails.setAmount(data.getAmount());
        paymentDetails.setName(data.getOwnerName());
        paymentDetails.setPaymentDate(HelperUtils.getCurrentTimeStamp());
        paymentDetails.setPaymentStatus("CO");
        paymentDetails.setPhone(data.getOwnerNumber());
        paymentDetails.setProductInfo("product_Info");
        paymentDetails.setTxnId(data.getOrderId());
        paymentDetails.setEmail("");

        paymentDetailsRepositry.save(paymentDetails);


        UpdatePaymentResponse updatePaymentResponse = new UpdatePaymentResponse();
        updatePaymentResponse.setMsg("Payment Info insert successfully");


        return ResponseUtils.createSuccessResponse(updatePaymentResponse, new TypeReference<UpdatePaymentResponse>() {
        });
    }

    @Override
    public ApiResponse<UpdatePaymentResponse> updatePaymentInfo1(UpdatePaymentRequest mainFormRequest) {

        MainForm getMainFormData = mainFromRepositry.findByFormId(mainFormRequest.getFormId());
        if (getMainFormData == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/14/" + mainFormRequest.getFormId());
        }

        if (getMainFormData.getOrderId() == null || getMainFormData.getOrderId().isEmpty()) {
            getMainFormData.setOrderId(mainFormRequest.getOrderId());
        }


        //        getMainFormData.setPaymentStatus("CO");
        //        getMainFormData.setTransactionData(mainFormRequest.getTxnData());
        //        getMainFormData.setTxnId(ConverterUtils.getRandomTimeStamp());
        //        getMainFormData.setOrderId(mainFormRequest.getOrderId());
        MainForm data = mainFromRepositry.save(getMainFormData);


        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentId(HelperUtils.getPaymentId());
        paymentDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        paymentDetails.setIsFlag(1);
        paymentDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        paymentDetails.setAmount(data.getAmount());
        paymentDetails.setName(data.getOwnerName());
        paymentDetails.setPaymentDate(HelperUtils.getCurrentTimeStamp());
        paymentDetails.setPaymentStatus("PE");
        paymentDetails.setPhone(data.getOwnerNumber());
        paymentDetails.setProductInfo("product_Info");
        paymentDetails.setTxnId(data.getOrderId());
        paymentDetails.setEmail("");
        paymentDetailsRepositry.save(paymentDetails);


        UpdatePaymentResponse updatePaymentResponse = new UpdatePaymentResponse();
        updatePaymentResponse.setMsg("Payment Info insert successfully");


        return ResponseUtils.createSuccessResponse(updatePaymentResponse, new TypeReference<UpdatePaymentResponse>() {
        });
    }

    @Override
    public ApiResponse<MainFormResponse> generatePdfFile(MainFormRequest mainFormRequest) {
        MainFormResponse mainFormResponse = new MainFormResponse();


        try {
            String str = "THE HABIT OF PERSISTENCE IS THE HABIT OF VICTORY.";


            String path = new File(".").getCanonicalPath() + "/Quote.png";

            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            generateQRcode(str, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("QR Code created successfully.");


        return ResponseUtils.createSuccessResponse(mainFormResponse, new TypeReference<MainFormResponse>() {
        });

    }

    @Override
    public ApiResponse<MainFormResponse> readCsv(MainFormRequest mainFormRequest) {
        MainFormResponse mainFormResponse = new MainFormResponse();


        //            MainForm getMainFormData = mainFromRepositry.findByFormId(mainFormRequest.getFormId());


        try {
            int count = 0;
            int count1 = 0;
            Reader in = new FileReader("/Users/apple/Documents/NAPR/test.csv");
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                String paymentStatus = record.get(4);
                String customerName = record.get(5);
                String customerMobileNo = record.get(10);
                String orderId = record.get(0);

                count1++;
                System.out.println(count1 + "  kkkkk ");
                if (paymentStatus.equalsIgnoreCase("Captured")) {


                    MainForm getMainFormData = mainFromRepositry.findByOrderId(orderId);

                    if (getMainFormData != null) {
                        if (getMainFormData.getPaymentStatus().equalsIgnoreCase("CO")) {
                            count++;
                            System.out.println(count + "ssss " + orderId);
                        } else {
                            System.out.println(getMainFormData.getFormId());
                        }
                    } else {


                    }

                }


            }


        } catch (Exception e) {

        }


        return ResponseUtils.createSuccessResponse(mainFormResponse, new TypeReference<MainFormResponse>() {
        });

    }

    @Override
    public ApiResponse<List<MainFormResponse>> getAllApprovePet() {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusAndApproverIdAndVerifierState(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "CO", "1", "AP");

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);

            MainFormResponse mainFormData = new MainFormResponse();
            MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();


            Long daysDiffrens = ConverterUtils.timeDiffer(data.getExpiryDate());
            if (daysDiffrens <= 20) {
                mainFormData.setRegistrationExpire("Registration Expired Soon");
            } else if (daysDiffrens <= 0) {
                mainFormData.setRegistrationExpire("Registration Expired");
            } else {
                mainFormData.setRegistrationExpire(daysDiffrens.toString() +" Day Remaining");
            }
            mainFormData.setDayRemaining("" + daysDiffrens);

            BeanUtils.copyProperties(data, mainFormData);

            MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
            CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());
            if (docData != null) {
                BeanUtils.copyProperties(docData, mainDocFormResponse);
                mainDocFormResponse.setDoc_form_id(docData.getDocFormId());
            }
            mainFormData.setMainDocFormResponse(mainDocFormResponse);

            mainFormData.setBreedName(brredType.getDescr());

            List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
            if (paymentInfo.size() > 0) {
                mainFormData.setTransactionData(paymentInfo.get(0));
            }

            mainFormResponses.add(mainFormData);
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });
    }


    public static String readQRcode(String path, String charset, Map map) throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
        Result rslt = new MultiFormatReader().decode(binaryBitmap);
        return rslt.getText();
    }


    public static void readQrData() throws WriterException, IOException, NotFoundException {
        String path = "C:\\Users\\Anubhav\\Desktop\\QRDemo\\Quote.png";
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        System.out.println("Data stored in the QR Code is: \n" + readQRcode(path, charset, hintMap));
    }

    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }


    @Override
    public ApiResponse<List<MainFormResponse>> getAllPendingForm(String mobileNo) {
        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusIsNull(mobileNo);
        Map<String, String> myMap = new HashMap<String, String>();
        List<MainForm> copyData = new ArrayList<MainForm>();

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);


            if (!myMap.containsKey(data.getNickName())) {
                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);
                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());
                BeanUtils.copyProperties(docData, mainDocFormResponse);
                mainFormData.setMainDocFormResponse(mainDocFormResponse);
                mainFormData.setBreedName(brredType.getDescr());

                List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                if (paymentInfo.size() > 0) {
                    mainFormData.setTransactionData(paymentInfo.get(0));
                }


                mainFormResponses.add(mainFormData);
                myMap.put(data.getNickName(), data.getNickName());
                myMap.put(data.getDob(), data.getDob());
            } else {
                copyData.add(data);
            }
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });
    }

    @Override
    public ApiResponse<List<MainFormResponse>> getAllUnpaidFormForm(String mobileNo) {
        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusIsNull(mobileNo);
        Map<String, String> myMap = new HashMap<String, String>();
        List<MainForm> copyData = new ArrayList<MainForm>();

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);


            if (!myMap.containsKey(data.getNickName())) {
                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);
                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());
                BeanUtils.copyProperties(docData, mainDocFormResponse);
                mainFormData.setMainDocFormResponse(mainDocFormResponse);
                mainFormData.setBreedName(brredType.getDescr());

                List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                if (paymentInfo.size() > 0) {
                    mainFormData.setTransactionData(paymentInfo.get(0));
                }
                mainFormResponses.add(mainFormData);
                myMap.put(data.getNickName(), data.getNickName());
                myMap.put(data.getDob(), data.getDob());
            } else {
                copyData.add(data);
            }
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });
    }

//    @Override
//    public ApiResponse<CheckAndUpdatePaymentResponse> updateForm(String formId) {
//        CheckAndUpdatePaymentResponse checkAndUpdatePaymentResponse = new CheckAndUpdatePaymentResponse();
//        MainForm data = mainFromRepositry.findByFormId(formId);
//
//        if (data == null) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID FORM ID");
//        }
//        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusIsNull(data.getOwnerNumber());
//        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
//            MainForm dataSet = getAllMainFormData.get(i);
//
//            if (dataSet.getFormId().equalsIgnoreCase(formId)) {
//                dataSet.setTransactionData("{payuResponse={\"id\":15822316076,\"mode\":\"UPI\",\"status\":\"success\",\"unmappedstatus\":\"captured\",\"key\":\"4gcXBD\",\"txnid\":\" " + dataSet.getOrderId() + "\",\"transaction_fee\":\"500.00\",\"amount\":\"500.00\",\"discount\":\"0.00\",\"addedon\":\"2022-09-09 15:03:16\",\"productinfo\":\"NAPR\",\"firstname\":\"By Admin\",\"email\":\"napr@gmail.com\",\"phone\":\"123123\",\"hash\":\"\",\"field1\":\"8368853253@ybl\",\"field2\":\"55883184218\",\"field3\":\" \",\"field4\":\"AMRITA KAUTS\",\"field5\":\"HDF30405C96BC3646B683FF4E55889E84F9\",\"field6\":\"ICICI Bank!414501504539!ICIC0004145!918368853253\",\"field7\":\"APPROVED OR COMPLETED SUCCESSFULLY|00\",\"field9\":\"SUCCESS|Completed Using Callback\",\"payment_source\":\"payu\",\"PG_TYPE\":\"UPI-PG\",\"bank_ref_no\":\"225273005170\",\"ibibo_code\":\"UPI\",\"error_code\":\"E000\",\"Error_Message\":\"No Error\",\"is_seamless\":1,\"surl\":\"https://payu.herokuapp.com/success\",\"furl\":\"https://payu.herokuapp.com/failure\"}, merchantResponse=null}");
//                dataSet.setPaymentStatus("CO");
//                dataSet.setTxnId("" + HelperUtils.getCurrentTimeStamp());
//                mainFromRepositry.save(dataSet);
//            } else {
//                dataSet.setPaymentStatus("DP");
//                mainFromRepositry.save(dataSet);
//            }
//        }
//
//        checkAndUpdatePaymentResponse.setMsg("Record Update sucessfully");
//
//        return ResponseUtils.createSuccessResponse(checkAndUpdatePaymentResponse, new TypeReference<CheckAndUpdatePaymentResponse>() {
//        });
//    }

//    @Override
//    public ApiResponse<CheckAndUpdatePaymentResponse> checkAndUpdatePaymentInfo(String paymentInfo) {
//
//        Map<String, String> myMap = new HashMap<String, String>();
//        CheckAndUpdatePaymentResponse checkAndUpdatePaymentResponse = new CheckAndUpdatePaymentResponse();
//        String[] pairs = paymentInfo.split("&");
//        for (int i = 0; i < pairs.length; i++) {
//            String pair = pairs[i];
//            String[] keyValue = pair.split("=");
//            myMap.put(keyValue[0], keyValue[1]);
//        }
//
//        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndOrderIdAndPaymentStatusIsNull(myMap.get("phone"), myMap.get("txnid"));
//        if (getAllMainFormData == null) {
//            checkAndUpdatePaymentResponse.setMsg("No data found");
//        } else {
//            for (Integer i = 0; i < getAllMainFormData.size(); i++) {
//
//                MainForm data = getAllMainFormData.get(i);
//                data.setTransactionData(myMap.toString());
//                data.setPaymentStatus("CO");
//                data.setTxnId(myMap.get("txnid"));
//                mainFromRepositry.save(data);
//            }
//            checkAndUpdatePaymentResponse.setMsg("Record Update sucessfully");
//        }
//
//        return ResponseUtils.createSuccessResponse(checkAndUpdatePaymentResponse, new TypeReference<CheckAndUpdatePaymentResponse>() {
//        });
//
//    }

    @Override
    public ApiResponse<CheckAndUpdatePaymentResponse> getPaymentCallBack(HashMap<String, Object> paymentInfo) {

        CheckAndUpdatePaymentResponse checkAndUpdatePaymentResponse = new CheckAndUpdatePaymentResponse();
        PaymentSucessDetiails paymentSucessDetiails = new PaymentSucessDetiails();
        PaymentFailedDetiails paymentFailedDetiails = new PaymentFailedDetiails();
        String txnid = "";
        String udf3 = "";
        String status = "";
        String name = "";
        String phone = "";

        try {
            phone = paymentInfo.get("phone").toString();
        } catch (Exception e) {
            phone = "";
        }
        try {
            name = paymentInfo.get("firstname").toString() + " " + paymentInfo.get("lastname").toString();
        } catch (Exception e) {
            name = "";
        }
        try {
            status = paymentInfo.get("status").toString();
        } catch (Exception e) {
            status = "";
        }
        try {
            txnid = paymentInfo.get("txnid").toString();
        } catch (Exception e) {
            txnid = "";
        }

        try {
            udf3 = paymentInfo.get("udf3").toString();
        } catch (Exception e) {
            udf3 = "";
        }


        if (status.equalsIgnoreCase("success")) {


            paymentSucessDetiails.setDetailedId("PD_" + HelperUtils.getCurrentTimeStamp());
            paymentSucessDetiails.setPaymentStatus(status);
            paymentSucessDetiails.setIsChecked("0");
            paymentSucessDetiails.setIsPayment("1");
            paymentSucessDetiails.setMobileNumber(phone);
            paymentSucessDetiails.setName(name);
            paymentSucessDetiails.setResponse(paymentInfo.toString());
            paymentSucessDetiails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            paymentSucessDetiails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            paymentSucessDetiails.setOrderId(txnid);
            paymentSucessDetiails.setTransId(txnid);


            try {

                String strNew = txnid.replaceAll("([A-Z])", "");
                strNew = strNew.replaceAll("([a-z])", "");
                strNew = strNew.replaceAll("([/_])", "");
                String orderIdNew = txnid.replaceAll("([/_])", "");

                MainForm mainFormData = mainFromRepositry.findByFormId(udf3);
                if (mainFormData != null) {

                    mainFormData.setPaymentStatus("CO");
                    mainFormData.setTransactionData(paymentSucessDetiails.getResponse());
                    mainFormData.setTxnId(txnid);
                    System.out.println("Data " + mainFormData.getFormId());
                    mainFromRepositry.save(mainFormData);

                    paymentSucessDetiails.setIsChecked("1");
                    paymentSucessRepoRepositry.save(paymentSucessDetiails);
                } else {
                    List<MainForm> getAllMainFormData = mainFromRepositry.findByOrderIdContainingOrOrderIdContainingAndPaymentStatusIsNullOrderByCreatedOnDesc(strNew, orderIdNew);

                    if (getAllMainFormData.size() > 0) {
                        MainForm mainForm = getAllMainFormData.get(0);
                        mainForm.setPaymentStatus("CO");
                        mainForm.setTransactionData(paymentSucessDetiails.getResponse());
                        mainForm.setTxnId(txnid);
                        System.out.println("Data " + mainForm.getFormId());
                        mainFromRepositry.save(mainForm);

                        paymentSucessDetiails.setIsChecked("1");
                        paymentSucessRepoRepositry.save(paymentSucessDetiails);

                    } else {
                        List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnIdContainingOrderByCreatedOnDesc(strNew);
                        if (paymentDetails.size() > 0) {
                            PaymentDetails paymentDetails1 = paymentDetails.get(0);
                            List<MainForm> getData = mainFromRepositry.findByOwnerNumber(paymentDetails1.getPhone());

                            if (getData.size() > 0) {


                                for (Integer i = 0; i < getData.size(); i++) {
                                    MainForm mainForm = getData.get(i);
                                    if (mainForm.getOrderId().contains(strNew)) {
                                        if (mainForm.getPaymentStatus().equalsIgnoreCase("CO")) {
                                            paymentSucessDetiails.setIsChecked("1");
                                            paymentSucessRepoRepositry.save(paymentSucessDetiails);
                                        } else {

                                            System.out.println("Data " + mainForm.getOrderId());
                                            mainForm.setPaymentStatus("CO");
                                            mainForm.setTransactionData(paymentInfo.toString());
                                            mainForm.setTxnId(txnid);
                                            System.out.println("Data " + mainForm.getFormId());
                                            mainFromRepositry.save(mainForm);
                                        }
                                    }
                                }
                            } else {
                                paymentSucessDetiails.setIsChecked("0");
                                paymentSucessRepoRepositry.save(paymentSucessDetiails);
                            }
                        } else {
                            paymentSucessDetiails.setIsChecked("0");
                            paymentSucessRepoRepositry.save(paymentSucessDetiails);
                        }
                    }

                }


            } catch (Exception e) {
                paymentSucessDetiails.setIsChecked("0");
                paymentSucessRepoRepositry.save(paymentSucessDetiails);
            }

            checkAndUpdatePaymentResponse.setMsg("Data capture successfully_AR/PS/001");

        } else {
            paymentFailedDetiails.setDetailedId("PD_" + HelperUtils.getCurrentTimeStamp());
            paymentFailedDetiails.setPaymentStatus(status);
            paymentFailedDetiails.setIsChecked("0");
            paymentFailedDetiails.setIsPayment("0");
            paymentFailedDetiails.setMobileNumber(phone);
            paymentFailedDetiails.setName(name);
            paymentFailedDetiails.setResponse(paymentInfo.toString());
            paymentFailedDetiails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            paymentFailedDetiails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            paymentFailedDetiails.setOrderId(txnid);
            paymentFailedDetiails.setTransId(txnid);
            paymentFailedRepoRepositry.save(paymentFailedDetiails);

            checkAndUpdatePaymentResponse.setMsg("Data capture successfully_AR/PF/002");
        }

        System.out.println("id" + paymentInfo + "");
        System.out.println("id" + "working");


        return ResponseUtils.createSuccessResponse(checkAndUpdatePaymentResponse, new TypeReference<CheckAndUpdatePaymentResponse>() {
        });
    }

    @Override
    public ApiResponse<CheckAndUpdatePaymentResponse> getPaymentCallBack1(String paymentInfo) {
        CheckAndUpdatePaymentResponse checkAndUpdatePaymentResponse = new CheckAndUpdatePaymentResponse();
        checkAndUpdatePaymentResponse.setMsg(paymentInfo + "Data capture successfully_AR0");
        return ResponseUtils.createSuccessResponse(checkAndUpdatePaymentResponse, new TypeReference<CheckAndUpdatePaymentResponse>() {
        });
    }

    @Override
    public ApiResponse<List<MainFormResponse>> getAllPendingPet(String mobileNo) {
        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        if (mobileNo == null || mobileNo.isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID FORM ID");
        }


        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusIsNullAndIsPendingList(mobileNo, "0");
        Map<String, String> myMap = new HashMap<String, String>();

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);

            if (!myMap.containsKey(data.getOrderId())) {


                if ((data.getAmount().equalsIgnoreCase("0") || data.getPaymentStatus() == null) && data.getBreedId().getBreedId().equalsIgnoreCase("breed_Id_166")) {

                    MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                    if (docData == null) {
                        MainFormResponse mainFormData = new MainFormResponse();
                        MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                        data.setOrderId(HelperUtils.getOrderId());
                        MainForm dataSet = mainFromRepositry.save(data);


                        List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnIdContainingOrderByCreatedOnDesc(data.getOrderId());
                        if (paymentDetails.size() > 0) {
                            PaymentDetails paymentDetails1Dtaa = paymentDetails.get(0);
                            paymentDetails1Dtaa.setTxnId(dataSet.getOrderId());
                            paymentDetailsRepositry.save(paymentDetails1Dtaa);
                        }


                        BeanUtils.copyProperties(dataSet, mainFormData);
                        CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());

                        if (docData != null) {
                            BeanUtils.copyProperties(docData, mainDocFormResponse);
                            mainDocFormResponse.setDoc_form_id(docData.getDocFormId());
                        }
                        mainFormData.setMainDocFormResponse(mainDocFormResponse);
                        List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                        if (paymentInfo.size() > 0) {
                            mainFormData.setTransactionData(paymentInfo.get(0));
                        }

                        mainFormData.setBreedName(brredType.getDescr());
                        mainFormResponses.add(mainFormData);


                    } else {
                        data.setAmount("0");
                        data.setPaymentStatus("CO");
                        data.setIsPendingList("1");
                        data.setIsUpdate("1");
                        mainFromRepositry.save(data);
                    }


                } else {

                    MainFormResponse mainFormData = new MainFormResponse();
                    MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                    data.setOrderId(HelperUtils.getOrderId());
                    MainForm dataSet = mainFromRepositry.save(data);

                    List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnIdContainingOrderByCreatedOnDesc(data.getOrderId());
                    if (paymentDetails.size() > 0) {
                        PaymentDetails paymentDetails1Dtaa = paymentDetails.get(0);
                        paymentDetails1Dtaa.setTxnId(dataSet.getOrderId());
                        paymentDetailsRepositry.save(paymentDetails1Dtaa);
                    }

                    BeanUtils.copyProperties(dataSet, mainFormData);
                    MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                    CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());


                    if (docData != null) {
                        BeanUtils.copyProperties(docData, mainDocFormResponse);
                        mainDocFormResponse.setDoc_form_id(docData.getDocFormId());

                    }
                    mainFormData.setMainDocFormResponse(mainDocFormResponse);
                    List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                    if (paymentInfo.size() > 0) {
                        mainFormData.setTransactionData(paymentInfo.get(0));
                    }

                    mainFormData.setBreedName(brredType.getDescr());
                    mainFormResponses.add(mainFormData);

                }

                myMap.put(data.getNickName(), data.getNickName());
                myMap.put(data.getDob(), data.getDob());
                myMap.put(data.getOrderId(), data.getOrderId());
            }
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });
    }

    @Override
    public ApiResponse<List<MainFormResponse>> getUpdateIsPendingList(String formId) {
        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        if (formId == null || formId.isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID FORM ID");
        }
        MainForm getAllMainFormUpdateData = mainFromRepositry.findByFormId(formId);


        if (getAllMainFormUpdateData == null) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID FORM ID");
        }

        getAllMainFormUpdateData.setIsPendingList("1");
        mainFromRepositry.save(getAllMainFormUpdateData);


        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusIsNullAndIsPendingList(getAllMainFormUpdateData.getOwnerNumber(), "0");
        Map<String, String> myMap = new HashMap<String, String>();

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);

            if (!myMap.containsKey(data.getOrderId())) {
                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);
                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());

                if (docData != null) {
                    BeanUtils.copyProperties(docData, mainDocFormResponse);
                    mainDocFormResponse.setDoc_form_id(docData.getDocFormId());

                }
                mainFormData.setMainDocFormResponse(mainDocFormResponse);
                List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                if (paymentInfo.size() > 0) {
                    mainFormData.setTransactionData(paymentInfo.get(0));
                }

                mainFormData.setBreedName(brredType.getDescr());
                mainFormResponses.add(mainFormData);


                myMap.put(data.getNickName(), data.getNickName());
                myMap.put(data.getDob(), data.getDob());
                myMap.put(data.getOrderId(), data.getOrderId());
            }
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });
    }

    @Override
    public ApiResponse<MainDocForm> fileUplaod1(MultipartFile pet1, String formId, String type) {
        MainDocForm uplaodDocuments = new MainDocForm();
        try {
            String mainFilePath = "data/mcpets/formSupprotDoc/" + formId + "/";

            if (pet1 == null || pet1.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo1 can not be blank.");
            }

            if (formId == null || formId.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form ID can not be blank.");
            }

            if (type == null || type.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Type ID can not be blank.");
            }

            MainForm getMainFormData = mainFromRepositry.findByFormId(formId);
            if (getMainFormData == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/15/" + formId);
            }
            MainDocForm saveData = mainDocFromRepositry.findByFormId(formId);
            if (saveData == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/15/" + formId);
            }


            String fileExtension1 = ConverterUtils.getFileExtension(pet1);


            if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")
            ) {

            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
            }


            if (type.equalsIgnoreCase("1")) {
                String filename1 = ConverterUtils.getRandomString("dog1");
                File targetFile1 = ConverterUtils.getTargetFile(fileExtension1, filename1, mainFilePath + "dog/");
                File mainPath1 = ConverterUtils.getComplaintPathOnly(fileExtension1, filename1, mainFilePath + "dog/");
                Path path1 = Paths.get(targetFile1.toString());
                InputStream in1 = new ByteArrayInputStream(pet1.getBytes());
                try {
                    System.out.println(
                            "Number of bytes copied1: "
                                    + Files.copy(
                                    in1, path1.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                saveData.setUpload_dog1_url(mainPath1.toString());


            } else if (type.equalsIgnoreCase("2")) {
                String fileExtension2 = ConverterUtils.getFileExtension(pet1);
                String filename2 = ConverterUtils.getRandomString("dogw");
                File targetFile2 = ConverterUtils.getTargetFile(fileExtension2, filename2, mainFilePath + "dog/");
                File mainPath2 = ConverterUtils.getComplaintPathOnly(fileExtension2, filename2, mainFilePath + "dog/");
                Path path2 = Paths.get(targetFile2.toString());
                InputStream in2 = new ByteArrayInputStream(pet1.getBytes());
                try {
                    System.out.println(
                            "Number of bytes copied2: "
                                    + Files.copy(
                                    in2, path2.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                saveData.setUpload_dog2_url(mainPath2.toString());


            } else if (type.equalsIgnoreCase("3")) {

                String fileExtension3 = ConverterUtils.getFileExtension(pet1);
                String filename3 = ConverterUtils.getRandomString("dog2");
                File targetFile3 = ConverterUtils.getTargetFile(fileExtension3, filename3, mainFilePath + "dog/");
                File mainPath3 = ConverterUtils.getComplaintPathOnly(fileExtension3, filename3, mainFilePath + "dog/");
                Path path3 = Paths.get(targetFile3.toString());
                InputStream in3 = new ByteArrayInputStream(pet1.getBytes());

                try {
                    System.out.println(
                            "Number of bytes copied3: "
                                    + Files.copy(
                                    in3, path3.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                saveData.setUpload_dog3_url(mainPath3.toString());


            } else if (type.equalsIgnoreCase("4")) {
                String fileExtension4 = ConverterUtils.getFileExtension(pet1);
                String filename4 = ConverterUtils.getRandomString("photoId");
                File targetFile4 = ConverterUtils.getTargetFile(fileExtension4, filename4, mainFilePath + "uploadDocs/");
                File mainPath4 = ConverterUtils.getComplaintPathOnly(fileExtension4, filename4, mainFilePath + "uploadDocs/");
                Path path4 = Paths.get(targetFile4.toString());
                InputStream in4 = new ByteArrayInputStream(pet1.getBytes());


                try {
                    System.out.println(
                            "Number of bytes copied4: "
                                    + Files.copy(
                                    in4, path4.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                saveData.setUpload_id_url(mainPath4.toString());


            } else if (type.equalsIgnoreCase("5")) {
                String fileExtension5 = ConverterUtils.getFileExtension(pet1);
                String filename5 = ConverterUtils.getRandomString("ownerPhoto");
                File targetFile5 = ConverterUtils.getTargetFile(fileExtension5, filename5, mainFilePath + "uploadDocs/");
                File mainPath5 = ConverterUtils.getComplaintPathOnly(fileExtension5, filename5, mainFilePath + "uploadDocs/");
                Path path5 = Paths.get(targetFile5.toString());
                InputStream in5 = new ByteArrayInputStream(pet1.getBytes());

                try {
                    System.out.println(
                            "Number of bytes copied5: "
                                    + Files.copy(
                                    in5, path5.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveData.setUpload_photo_url(mainPath5.toString());


            } else if (type.equalsIgnoreCase("6")) {
                String fileExtension6 = ConverterUtils.getFileExtension(pet1);
                String filename6 = ConverterUtils.getRandomString("sign");
                File targetFile6 = ConverterUtils.getTargetFile(fileExtension6, filename6, mainFilePath + "uploadDocs/");
                File mainPath6 = ConverterUtils.getComplaintPathOnly(fileExtension6, filename6, mainFilePath + "uploadDocs/");
                Path path6 = Paths.get(targetFile6.toString());
                InputStream in6 = new ByteArrayInputStream(pet1.getBytes());


                try {
                    System.out.println(
                            "Number of bytes copied6: "
                                    + Files.copy(
                                    in6, path6.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                saveData.setUpload_sign_url(mainPath6.toString());


            } else if (type.equalsIgnoreCase("7")) {
                String fileExtension7 = ConverterUtils.getFileExtension(pet1);
                String filename7 = ConverterUtils.getRandomString("addressProof");
                File targetFile7 = ConverterUtils.getTargetFile(fileExtension7, filename7, mainFilePath + "uploadDocs/");
                File mainPath7 = ConverterUtils.getComplaintPathOnly(fileExtension7, filename7, mainFilePath + "uploadDocs/");
                Path path7 = Paths.get(targetFile7.toString());
                InputStream in7 = new ByteArrayInputStream(pet1.getBytes());


                try {
                    System.out.println(
                            "Number of bytes copied7: "
                                    + Files.copy(
                                    in7, path7.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveData.setUpload_id_proof(mainPath7.toString());


            } else if (type.equalsIgnoreCase("8")) {
                String fileExtension8 = ConverterUtils.getFileExtension(pet1);

                String filename8 = ConverterUtils.getRandomString("vaccination");
                File targetFile8 = ConverterUtils.getTargetFile(fileExtension8, filename8, mainFilePath + "uploadDocs/");
                File mainPath8 = ConverterUtils.getComplaintPathOnly(fileExtension8, filename8, mainFilePath + "uploadDocs/");
                Path path8 = Paths.get(targetFile8.toString());
                InputStream in8 = new ByteArrayInputStream(pet1.getBytes());
                try {

                    System.out.println(
                            "Number of bytes copied8: "
                                    + Files.copy(
                                    in8, path8.toAbsolutePath(),
                                    StandardCopyOption.REPLACE_EXISTING));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                saveData.setUpload_valid_book(mainPath8.toString());

            } else if (type.equalsIgnoreCase("9")) {

            }


            MainDocForm mainDocForm = mainDocFromRepositry.save(saveData);

            return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<MainDocForm>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        uplaodDocuments.setMeassge("File upload successfully");
        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<MainDocForm>() {
        });
    }

    @Override
    public ApiResponse<UpdatePaymentResponse> updateNeutering(UpdatePaymentRequest mainFormRequest) {

        if (mainFormRequest.getFormId() == null || mainFormRequest.getFormId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "FORM ID CAN NOT BE BLANK.");
        }
        if (mainFormRequest.getIsNeutering() == null || mainFormRequest.getIsNeutering().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "NEUTERING CAN NOT BE BLANK.");
        }

        MainForm getMainFormData = mainFromRepositry.findByFormId(mainFormRequest.getFormId());

        getMainFormData.setIsNeutering(mainFormRequest.getIsNeutering());
        mainFromRepositry.save(getMainFormData);

        UpdatePaymentResponse updatePaymentResponse = new UpdatePaymentResponse();
        updatePaymentResponse.setMsg("Data Update Successfully");


        return ResponseUtils.createSuccessResponse(updatePaymentResponse, new TypeReference<UpdatePaymentResponse>() {
        });
    }

    @Override
    public ApiResponse<List<MainFormResponse>> getAllgetAllPetNeutering() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatus(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "CO");

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);

            Boolean splitAge = ConverterUtils.getPetAge(data.getDob().replaceAll("([A-Z])", "").replaceAll("([a-z])", ""));

            if (splitAge) {
                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);

                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());

                if (docData != null) {
                    BeanUtils.copyProperties(docData, mainDocFormResponse);
                    mainDocFormResponse.setDoc_form_id(docData.getDocFormId());
                }

                mainFormData.setMainDocFormResponse(mainDocFormResponse);
                mainFormData.setBreedName(brredType.getDescr());

                List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                if (paymentInfo.size() > 0) {
                    mainFormData.setTransactionData(paymentInfo.get(0));
                }

                mainFormResponses.add(mainFormData);
            }

        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });

    }

    @Override
    public ApiResponse<List<MainFormResponse>> getRenewalData() {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusAndIsRenewed(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "CO", 0);

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {

            MainForm data = getAllMainFormData.get(i);

            Long daysDiffrens = ConverterUtils.timeDiffer(data.getExpiryDate());
            System.out.println("daysDiffrens" + daysDiffrens);

            if (daysDiffrens <= 20) {
                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);

                if (daysDiffrens <= 0) {
                    mainFormData.setRegistrationExpire("Registration Expired");
                } else if (daysDiffrens <= 20) {
                    mainFormData.setRegistrationExpire("Registration Expired Soon");
                } else {
                    mainFormData.setRegistrationExpire("");
                }
                mainFormData.setDayRemaining("" + daysDiffrens);

                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());
                if (docData != null) {
                    BeanUtils.copyProperties(docData, mainDocFormResponse);
                    mainDocFormResponse.setDoc_form_id(docData.getDocFormId());
                }
                mainFormData.setMainDocFormResponse(mainDocFormResponse);

                List<PaymentDetails> paymentInfo = paymentDetailsRepositry.findByTxnId(data.getOrderId());
                if (paymentInfo.size() > 0) {
                    mainFormData.setTransactionData(paymentInfo.get(0));
                }
                CodeBreedType brredType = codeBreedTypeRepositry.findByBreedId(data.getBreedId().getBreedId());
                mainFormData.setBreedName(brredType.getDescr());
                mainFormResponses.add(mainFormData);
            }
        }

        return ResponseUtils.createSuccessResponse(mainFormResponses, new TypeReference<List<MainFormResponse>>() {
        });


    }

    @Override
    public ApiResponse<MainFormResponse> renewalMainForm1(RenewalFormRequest mainFormRequest) {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }


        if (mainFormRequest.getRabbiesDate() == null || mainFormRequest.getRabbiesDate().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER RABBIS DATE CAN NOT BE BLANK");
        }

        if (mainFormRequest.getDob() == null || mainFormRequest.getDob().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OWNER PET DOB CAN NOT BE BLANK");
        }

        if (mainFormRequest.getNextRabbiesDate() == null || mainFormRequest.getNextRabbiesDate().toString().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "NEXT RABIES DATE CAN NOT BE BLANK");
        }


        MainForm getMainFormOldData = mainFromRepositry.findByFormId(mainFormRequest.getFormId());
        if (getMainFormOldData == null) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID FORM ID");
        }
        getMainFormOldData.setIsRenewed(1);
        getMainFormOldData.setUpdatedOn(HelperUtils.getCurrentTimeStamp());

        MainDocForm mainDocForm = mainDocFromRepositry.findByFormId(getMainFormOldData.getFormId());
        if (mainDocForm == null) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID DOCUMENT ID ");
        }


        MainForm mainForm = new MainForm();
        CodeBreedType codeBreedType = codeBreedTypeRepositry.findByBreedId(getMainFormOldData.getBreedId().getBreedId());


        if (codeBreedType.getBreedId().equalsIgnoreCase("breed_Id_166")) {
            mainForm.setAmount("0");

        } else {
            mainForm.setAmount("500");
        }


        String orderId = HelperUtils.getOrderId();
        List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnId(orderId);
        if (paymentDetails.size() > 0) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "ORDER ID ALREADY EXIST" + getMainFormOldData.getOrderId());
        }


        mainForm.setFormId(HelperUtils.getFormId());
        mainForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        mainForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        mainForm.setIsFlag(1);
        mainForm.setAddress(mainFormRequest.getAddress());

        mainForm.setCreatorState("PE");
        mainForm.setDob(mainFormRequest.getDob());
        mainForm.setDogId(HelperUtils.getDogId());
        mainForm.setFormType("ON");
        mainForm.setSex(getMainFormOldData.getSex());
        mainForm.setIsRenewed(0);
        mainForm.setIsUpdate("0");
        mainForm.setNextRabbiesDate(ConverterUtils.convertDateTotimeStamp(mainFormRequest.getNextRabbiesDate()));
        mainForm.setNickName(getMainFormOldData.getNickName());

        mainForm.setOwnerNumber(getMainFormOldData.getOwnerNumber());
        mainForm.setOwnerName(getMainFormOldData.getOwnerName());
        mainForm.setRabbiesDate(ConverterUtils.convertDateTotimeStamp(mainFormRequest.getRabbiesDate()));
        mainForm.setVerifierState("IB");

        mainForm.setAddLine1(mainFormRequest.getAddLine1());
        mainForm.setAddressType(mainFormRequest.getAddressType());
        mainForm.setBlock(mainFormRequest.getBlock());
        mainForm.setFlatNo(mainFormRequest.getFlatNo());
        mainForm.setLandmark(mainFormRequest.getLandmark());
        mainForm.setExpiryDate(ConverterUtils.getNextYearDate());

        mainForm.setOrderId(orderId);
        mainForm.setTxnId(orderId);
        mainForm.setIsNeutering(mainFormRequest.getIsNeutering());

        mainForm.setPincode(mainFormRequest.getPincode());
        mainForm.setSocietyOther(getMainFormOldData.getSocietyOther());
        mainForm.setStreetNo(mainFormRequest.getStreetNo());
        mainForm.setTowerNo(mainFormRequest.getTowerNo());


        mainForm.setIsPendingList("0");
        mainForm.setIsUpdate("0");


        CodeAddressVillage codeAddressVillage = villageRepositry.findByVillageId(mainFormRequest.getVillageId());
        CodeFromCategory codeFromCategory = codeFromCategoryRepositry.findByCatIdOrderByDescrAsc(getMainFormOldData.getCatId().getCatId());
        MangerScoiety mangerScoiety = mangerScoietyRepositry.findByManageSocietyId(mainFormRequest.getManageSocietyId());
        CodeAddressSector codeAddressSector = codeAddressSectorRepositry.findBySectorId(mainFormRequest.getSectorId());
        UserAccount userAccount = userAccountRepositry.findByUserAccId(getMainFormOldData.getCreatorId().getUserAccId());


        mainForm.setSectorId(codeAddressSector);
        mainForm.setVillageId(codeAddressVillage);
        mainForm.setBreedId(codeBreedType);
        mainForm.setCatId(codeFromCategory);
        mainForm.setCreatorId(userAccount);
        mainForm.setManageSocietyId(mangerScoiety);


        MainForm saveData = mainFromRepositry.save(mainForm);
        mainFromRepositry.save(getMainFormOldData);

        MainFormResponse mainFormResponse = new MainFormResponse();
        mainFormResponse.setFormId(saveData.getFormId());
        mainFormResponse.setDocId(mainDocForm.getDocFormId());
        mainFormResponse.setOrderId(saveData.getOrderId());


        return ResponseUtils.createSuccessResponse(mainFormResponse, new TypeReference<MainFormResponse>() {
        });

    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> fileUplaodRenewal(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file7, MultipartFile file8, String formId, String docId) {
        UplaodMainFormDocumentsResponse uplaodDocuments = new UplaodMainFormDocumentsResponse();
        try {
            String mainFilePath = "data/mcpets/formSupprotDoc/" + formId + "/";

            if (pet1 == null || pet1.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo1 can not be blank.");
            }
            if (pet2 == null || pet2.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo2 can not be blank.");
            }

            if (pet3 == null || pet3.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Pet Photo3 can not be blank.");
            }

            if (file7 == null || file7.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo4 can not be blank.");
            }
            if (file8 == null || file8.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Photo3 can not be blank.");
            }
            if (formId == null || formId.isEmpty()) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form ID can not be blank.");
            }

            MainDocForm existMainDocForm = mainDocFromRepositry.findByDocFormId(docId);
            if (existMainDocForm == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form DOCUMENT ID/NA/15/" + formId);
            }


            MainForm getMainFormData = mainFromRepositry.findByFormId(formId);
            if (getMainFormData == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/15/" + formId);
            }


            String fileExtension1 = ConverterUtils.getFileExtension(pet1);
            String fileExtension2 = ConverterUtils.getFileExtension(pet2);
            String fileExtension3 = ConverterUtils.getFileExtension(pet3);
            String fileExtension7 = ConverterUtils.getFileExtension(file7);
            String fileExtension8 = ConverterUtils.getFileExtension(file8);


            if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")
                    || fileExtension2.equalsIgnoreCase(".jpeg") || fileExtension2.equalsIgnoreCase(".png") || fileExtension2.equalsIgnoreCase(".jpg")
                    || fileExtension3.equalsIgnoreCase(".jpeg") || fileExtension3.equalsIgnoreCase(".png") || fileExtension3.equalsIgnoreCase(".jpg")
                    || fileExtension7.equalsIgnoreCase(".jpeg") || fileExtension7.equalsIgnoreCase(".png") || fileExtension7.equalsIgnoreCase(".jpg")
                    || fileExtension8.equalsIgnoreCase(".jpeg") || fileExtension8.equalsIgnoreCase(".png") || fileExtension8.equalsIgnoreCase(".jpg")
            ) {

            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
            }

            String filename1 = ConverterUtils.getRandomString("dog1");
            String filename2 = ConverterUtils.getRandomString("dogw");
            String filename3 = ConverterUtils.getRandomString("dog2");
            String filename4 = ConverterUtils.getRandomString("photoId");
            String filename5 = ConverterUtils.getRandomString("ownerPhoto");
            String filename6 = ConverterUtils.getRandomString("sign");
            String filename7 = ConverterUtils.getRandomString("addressProof");
            String filename8 = ConverterUtils.getRandomString("vaccination");


            File targetFile1 = ConverterUtils.getTargetFile(fileExtension1, filename1, mainFilePath + "dog/");
            File targetFile2 = ConverterUtils.getTargetFile(fileExtension2, filename2, mainFilePath + "dog/");
            File targetFile3 = ConverterUtils.getTargetFile(fileExtension3, filename3, mainFilePath + "dog/");
            File targetFile7 = ConverterUtils.getTargetFile(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            File targetFile8 = ConverterUtils.getTargetFile(fileExtension8, filename8, mainFilePath + "uploadDocs/");


            File mainPath1 = ConverterUtils.getComplaintPathOnly(fileExtension1, filename1, mainFilePath + "dog/");
            File mainPath2 = ConverterUtils.getComplaintPathOnly(fileExtension2, filename2, mainFilePath + "dog/");
            File mainPath3 = ConverterUtils.getComplaintPathOnly(fileExtension3, filename3, mainFilePath + "dog/");
            File mainPath7 = ConverterUtils.getComplaintPathOnly(fileExtension7, filename7, mainFilePath + "uploadDocs/");
            File mainPath8 = ConverterUtils.getComplaintPathOnly(fileExtension8, filename8, mainFilePath + "uploadDocs/");


            Path path1 = Paths.get(targetFile1.toString());
            Path path2 = Paths.get(targetFile2.toString());
            Path path3 = Paths.get(targetFile3.toString());
            Path path7 = Paths.get(targetFile7.toString());
            Path path8 = Paths.get(targetFile8.toString());


            InputStream in1 = new ByteArrayInputStream(pet1.getBytes());
            InputStream in2 = new ByteArrayInputStream(pet2.getBytes());
            InputStream in3 = new ByteArrayInputStream(pet3.getBytes());
            InputStream in7 = new ByteArrayInputStream(file7.getBytes());
            InputStream in8 = new ByteArrayInputStream(file8.getBytes());

            try {
                System.out.println(
                        "Number of bytes copied1: "
                                + Files.copy(
                                in1, path1.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(
                        "Number of bytes copied2: "
                                + Files.copy(
                                in2, path2.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied3: "
                                + Files.copy(
                                in3, path3.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.out.println(
                        "Number of bytes copied7: "
                                + Files.copy(
                                in7, path7.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                System.out.println(
                        "Number of bytes copied8: "
                                + Files.copy(
                                in8, path8.toAbsolutePath(),
                                StandardCopyOption.REPLACE_EXISTING));

            } catch (IOException e) {
                e.printStackTrace();
            }


            MainDocForm mainDocForm = new MainDocForm();
            mainDocForm.setDocFormId(HelperUtils.getDocumentId());
            mainDocForm.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            mainDocForm.setIsFlag(1);
            mainDocForm.setFormId(formId);

            mainDocForm.setUpload_dog1_url(mainPath1.toString());
            mainDocForm.setUpload_dog2_url(mainPath2.toString());
            mainDocForm.setUpload_dog3_url(mainPath3.toString());
            mainDocForm.setUpload_id_proof(mainPath7.toString());
            mainDocForm.setUpload_id_url(existMainDocForm.getUpload_id_proof());
            mainDocForm.setUpload_photo_url(existMainDocForm.getUpload_photo_url());
            mainDocForm.setUpload_sign_url(existMainDocForm.getUpload_sign_url());
            mainDocForm.setUpload_valid_book(mainPath8.toString());


            uplaodDocuments.setDog1(path1.toString());
            uplaodDocuments.setOwnerWithPet(path2.toString());
            uplaodDocuments.setDog2(path3.toString());
            uplaodDocuments.setAddressPrrof(path7.toString());
            uplaodDocuments.setVacinationbook(path8.toString());


            try {
                MainForm mainForm = mainFromRepositry.findByFormId(formId);
                CodeBreedType codeBreedType = codeBreedTypeRepositry.findByBreedId(mainForm.getBreedId().getBreedId());
                List<MainForm> mainFormListData = mainFromRepositry.findByOwnerNumberAndPaymentStatusAndBreedId(mainForm.getOwnerNumber(), "CO", codeBreedType);
                if (mainFormListData.size() > 10) {
                    uplaodDocuments.setAmount("500");
                } else if (codeBreedType.getBreedId().equalsIgnoreCase("breed_Id_166")) {
                    uplaodDocuments.setAmount("0");

                    if (getMainFormData == null) {
                        throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/16/" + formId);
                    }
                    mainForm.setPaymentStatus("CO");
                    mainForm.setIsPendingList("1");
                    mainForm.setIsUpdate("1");
                    mainForm.setTxnId(mainForm.getOrderId());
                    mainFromRepositry.save(mainForm);

                    PaymentDetails paymentDetails = new PaymentDetails();
                    paymentDetails.setPaymentId(HelperUtils.getPaymentId());
                    paymentDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
                    paymentDetails.setIsFlag(1);
                    paymentDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
                    paymentDetails.setAmount(mainForm.getAmount());
                    paymentDetails.setName(mainForm.getOwnerName());
                    paymentDetails.setPaymentDate(HelperUtils.getCurrentTimeStamp());
                    paymentDetails.setPaymentStatus("CO");
                    paymentDetails.setPhone(mainForm.getOwnerNumber());
                    paymentDetails.setProductInfo("product_Info");
                    paymentDetails.setTxnId(mainForm.getOrderId());
                    paymentDetails.setEmail("");

                    paymentDetailsRepositry.save(paymentDetails);

                } else {
                    uplaodDocuments.setAmount("500");
                }

                MainDocForm saveData = mainDocFromRepositry.save(mainDocForm);
                uplaodDocuments.setDocId(saveData.getDocFormId());
            } catch (Exception e) {

            }
            uplaodDocuments.setMeassge("File upload successfully");
            return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        uplaodDocuments.setMeassge("File upload successfully");
        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }



}


