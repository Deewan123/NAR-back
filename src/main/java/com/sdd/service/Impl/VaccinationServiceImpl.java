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
import com.sdd.request.*;
import com.sdd.response.*;
import com.sdd.service.MainFormServices;
import com.sdd.service.VacciNationServices;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.io.Reader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.*;

@Service
@AllArgsConstructor
public class VaccinationServiceImpl implements VacciNationServices {


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
    private DoctorRepositry doctorRepositry;

    @Autowired
    private VacinationBookRepositry vacinationBookRepositry;

    @Autowired
    private VacinationSlotingRepositry vacinationSlotingRepositry;

    @Autowired
    private AppointmentRepositry appointmentRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;


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
    public ApiResponse<List<MainFormResponse>> getVacinationData() {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<MainFormResponse> mainFormResponses = new ArrayList<MainFormResponse>();
        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumberAndPaymentStatusAndIsRenewed(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "CO", 0);

        for (Integer i = 0; i < getAllMainFormData.size(); i++) {
            MainForm data = getAllMainFormData.get(i);

            Long daysDiffrens = 0l;
            if (data.getNextRabbiesDate() == null) {
                daysDiffrens = 0l;
            } else {
                daysDiffrens = ConverterUtils.timeDiffer(data.getNextRabbiesDate());
            }
            System.out.println("daysDiffrens" + daysDiffrens);

            if (daysDiffrens <= 20) {
                MainFormResponse mainFormData = new MainFormResponse();
                MainDocFormResponse mainDocFormResponse = new MainDocFormResponse();
                BeanUtils.copyProperties(data, mainFormData);

                if (daysDiffrens == 0) {
                    mainFormData.setRegistrationExpire("Vaccination Expired");
                } else if (daysDiffrens <= -1) {
                    mainFormData.setRegistrationExpire("Vaccination Due");
                } else if (daysDiffrens <= 30) {
                    mainFormData.setRegistrationExpire("Vaccination Expired Soon");
                } else {
                    mainFormData.setRegistrationExpire("");
                }
                mainFormData.setDayRemaining("" + daysDiffrens);
                mainFormData.setPenaltyAmount("0");

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
    public ApiResponse<List<DocterListResponse>> getAllDocterList(DocterListRequest docterListRequest) {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        ArrayList<DocterListResponse> docterListResponses = new ArrayList<DocterListResponse>();
        List<DocterDetiails> getAllDoctorDetails = doctorRepositry.findAll();

        for (Integer i = 0; i < getAllDoctorDetails.size(); i++) {
            DocterDetiails data = getAllDoctorDetails.get(i);
            DocterListResponse mainFormData = new DocterListResponse();
            BeanUtils.copyProperties(data, mainFormData);
            docterListResponses.add(mainFormData);
        }

        return ResponseUtils.createSuccessResponse(docterListResponses, new TypeReference<List<DocterListResponse>>() {
        });
    }


    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> bookVaccinationAppointment(AppointmentRequest appointmentRequest) {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        UplaodMainFormDocumentsResponse docterListResponse = new UplaodMainFormDocumentsResponse();

        if (appointmentRequest.getFormId() == null || appointmentRequest.getFormId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form Id can not be blank.");
        }
        if (appointmentRequest.getDoctorId() == null || appointmentRequest.getDoctorId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Doctor Id can not be blank.");
        }
        if (appointmentRequest.getAppointmentDate() == null || appointmentRequest.getAppointmentDate().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Appointment Date can not be blank.");
        }
        if (appointmentRequest.getMobileNumber() == null || appointmentRequest.getMobileNumber().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Mobile Number can not be blank.");
        }
        if (appointmentRequest.getSlottingtime() == null || appointmentRequest.getSlottingtime().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Slotting time can not be blank.");
        }



        MainForm mainForm = mainFromRepositry.findByFormId(appointmentRequest.getFormId());
        if (mainForm == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Form Id.Please Check");

        }



        List<BookVacinationAppDetiails> checkBookVacinationAppDetiails = vacinationBookRepositry.findByFormIdAndIsComplete(appointmentRequest.getFormId(), "0");
        if (checkBookVacinationAppDetiails.size() > 0) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Appointment Already Booked.Please Reschedule.");

        }


        ConverterUtils.checkGetIsvalidOrNor(appointmentRequest.getAppointmentDate());
//        ConverterUtils.checkGetIsvalidOrNor(appointmentRequest.getAppointmentCompleteDate());


        BookVacinationAppDetiails bookVacinationAppDetiails = new BookVacinationAppDetiails();
        bookVacinationAppDetiails.setAppointmentId(HelperUtils.getBooKingId());
        bookVacinationAppDetiails.setDoctorId(appointmentRequest.getDoctorId());
        bookVacinationAppDetiails.setAppointmentDate(ConverterUtils.convertDateTotimeStampBookAppointment(appointmentRequest.getAppointmentDate()));
//        bookVacinationAppDetiails.setAppointmentCompleteDate(ConverterUtils.convertDateTotimeStampBookAppointment(appointmentRequest.getAppointmentCompleteDate()));
        bookVacinationAppDetiails.setFormId(appointmentRequest.getFormId());
        bookVacinationAppDetiails.setMobileNumber(appointmentRequest.getMobileNumber());
        bookVacinationAppDetiails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        bookVacinationAppDetiails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        bookVacinationAppDetiails.setIsCancel("0");
        bookVacinationAppDetiails.setIsComplete("0");
        bookVacinationAppDetiails.setSlottingTime(appointmentRequest.getSlottingtime());
        bookVacinationAppDetiails.setUserCode(ConverterUtils.createdOtp());

        appointmentRepositry.save(bookVacinationAppDetiails);

        bookVaccination(appointmentRequest.getFormId(), appointmentRequest.getMobileNumber(), appointmentRequest.getAppointmentDate() + " " + appointmentRequest.getSlottingtime());

        docterListResponse.setMeassge("Appointment Book Successfully");
        return ResponseUtils.createSuccessResponse(docterListResponse, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });

    }

    @Override
    public ApiResponse<List<AppointmentResponse>> getUserAppointment() {

        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
        List<BookVacinationAppDetiails> getAllDoctorDetails = vacinationBookRepositry.findByMobileNumber(currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
        for (Integer i = 0; i < getAllDoctorDetails.size(); i++) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();

            BookVacinationAppDetiails bookVacinationAppDetiails = getAllDoctorDetails.get(i);
            MainForm mainForm = mainFromRepositry.findByFormId(bookVacinationAppDetiails.getFormId());
            DocterDetiails docterDetiails = doctorRepositry.findByDoctorId(bookVacinationAppDetiails.getDoctorId());

            appointmentResponse.setDocterDetiails(docterDetiails);
            appointmentResponse.setGetAllDoctorDetails(bookVacinationAppDetiails);
            appointmentResponse.setMainForm(mainForm);
            appointmentResponseList.add(appointmentResponse);
        }
        return ResponseUtils.createSuccessResponse(appointmentResponseList, new TypeReference<List<AppointmentResponse>>() {
        });
    }




    @Override
    public ApiResponse<List<AppointmentResponse>> getDoctorCompleteAppointment() {
        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        List<BookVacinationAppDetiails> getAllDoctorDetails = vacinationBookRepositry.findByDoctorIdAndIsCompleteAndIsCancel(currentLoggedInUser.get(HeaderUtils.USER_ID), "1", "0");
        for (Integer i = 0; i < getAllDoctorDetails.size(); i++) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();

            BookVacinationAppDetiails bookVacinationAppDetiails = getAllDoctorDetails.get(i);
            MainForm mainForm = mainFromRepositry.findByFormId(bookVacinationAppDetiails.getFormId());
            DocterDetiails docterDetiails = doctorRepositry.findByDoctorId(bookVacinationAppDetiails.getDoctorId());

            appointmentResponse.setDocterDetiails(docterDetiails);
            appointmentResponse.setGetAllDoctorDetails(bookVacinationAppDetiails);
            appointmentResponse.setMainForm(mainForm);
            appointmentResponseList.add(appointmentResponse);
        }


        return ResponseUtils.createSuccessResponse(appointmentResponseList, new TypeReference<List<AppointmentResponse>>() {
        });
    }

    @Override
    public ApiResponse<List<AppointmentResponse>> getDoctorPendingAppointment() {
        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        List<BookVacinationAppDetiails> getAllDoctorDetails = vacinationBookRepositry.findByDoctorIdAndIsCompleteAndIsCancel(currentLoggedInUser.get(HeaderUtils.USER_ID), "0", "0");
        for (Integer i = 0; i < getAllDoctorDetails.size(); i++) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();

            BookVacinationAppDetiails bookVacinationAppDetiails = getAllDoctorDetails.get(i);
            MainForm mainForm = mainFromRepositry.findByFormId(bookVacinationAppDetiails.getFormId());
            DocterDetiails docterDetiails = doctorRepositry.findByDoctorId(bookVacinationAppDetiails.getDoctorId());

            appointmentResponse.setDocterDetiails(docterDetiails);
            appointmentResponse.setGetAllDoctorDetails(bookVacinationAppDetiails);
            appointmentResponse.setMainForm(mainForm);
            appointmentResponseList.add(appointmentResponse);
        }

        return ResponseUtils.createSuccessResponse(appointmentResponseList, new TypeReference<List<AppointmentResponse>>() {
        });
    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> cancelAppointmentByDoctor(AppointmentCancelRequest appointmentCancelRequest) {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        UplaodMainFormDocumentsResponse uplaodMainFormDocumentsResponse = new UplaodMainFormDocumentsResponse();

        if (appointmentCancelRequest.getAppointmentId() == null || appointmentCancelRequest.getAppointmentId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Appointment Id can not be blank.");
        }
        if (appointmentCancelRequest.getCancelReason() == null || appointmentCancelRequest.getCancelReason().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Cancel Reason can not be blank.");
        }

        BookVacinationAppDetiails getAllDoctorDetails = vacinationBookRepositry.findByAppointmentId(appointmentCancelRequest.getAppointmentId());


        getAllDoctorDetails.setIsCancel("1");
        getAllDoctorDetails.setCancelReason(appointmentCancelRequest.getCancelReason());
        getAllDoctorDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());

        vacinationBookRepositry.save(getAllDoctorDetails);
        uplaodMainFormDocumentsResponse.setMeassge("Record Update Successfully");

        return ResponseUtils.createSuccessResponse(uplaodMainFormDocumentsResponse, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> appointmentCompleteByDoctor(AppointmentCancelRequest appointmentCancelRequest) {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        UplaodMainFormDocumentsResponse uplaodMainFormDocumentsResponse = new UplaodMainFormDocumentsResponse();

        if (appointmentCancelRequest.getAppointmentId() == null || appointmentCancelRequest.getAppointmentId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Appointment Id can not be blank.");
        }

        if (appointmentCancelRequest.getUserCode() == null || appointmentCancelRequest.getUserCode().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "User Code can not be blank.");
        }


        if (appointmentCancelRequest.getVaccinationDate() == null || appointmentCancelRequest.getVaccinationDate().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Vaccination Date can not be blank.");
        }

        if (appointmentCancelRequest.getNextVaccinationDate() == null || appointmentCancelRequest.getNextVaccinationDate().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Next Vaccination Date can not be blank.");
        }

//        ConverterUtils.checkGetIsvalidOrNor(appointmentCancelRequest.getVaccinationDate());
//        ConverterUtils.checkGetIsvalidOrNor(appointmentCancelRequest.getNextVaccinationDate());

        BookVacinationAppDetiails getAllDoctorDetails = vacinationBookRepositry.findByAppointmentId(appointmentCancelRequest.getAppointmentId());
        if (getAllDoctorDetails == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid AppointmentId");
        }

        if (!(appointmentCancelRequest.getUserCode().equalsIgnoreCase(getAllDoctorDetails.getUserCode()))) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid User code.");
        }


        MainForm mainForm = mainFromRepositry.findByFormId(appointmentCancelRequest.getFormId());
        if (mainForm == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Form Id");
        }

        ConverterUtils.checkGetIsvalidOrNor(appointmentCancelRequest.getVaccinationDate());
        ConverterUtils.checkGetIsvalidOrNor(appointmentCancelRequest.getNextVaccinationDate());

        mainForm.setRabbiesDate(ConverterUtils.convertDateTotimeStampBookAppointment(appointmentCancelRequest.getVaccinationDate()));
        mainForm.setNextRabbiesDate(ConverterUtils.convertDateTotimeStampBookAppointment(appointmentCancelRequest.getNextVaccinationDate()));

        mainFromRepositry.save(mainForm);


        getAllDoctorDetails.setIsCancel("0");
        getAllDoctorDetails.setIsComplete("1");
        getAllDoctorDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        vacinationBookRepositry.save(getAllDoctorDetails);


        uplaodMainFormDocumentsResponse.setMeassge("Record Complete Successfully");

        return ResponseUtils.createSuccessResponse(uplaodMainFormDocumentsResponse, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }

    @Override
    public ApiResponse<VaccinationSlotResponse> getVaccinationSlot(GetVaccinationSlotRequest getVaccinationSlotRequest) {


        ArrayList<VacinationSalotingDetiails> slotList = new ArrayList<VacinationSalotingDetiails>();
        VaccinationSlotResponse response = new VaccinationSlotResponse();


        if (getVaccinationSlotRequest.getDate() == null || getVaccinationSlotRequest.getDate().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Date can not be blank.");
        }

        if (getVaccinationSlotRequest.getDocId() == null || getVaccinationSlotRequest.getDocId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Doctor Id can not be blank.");
        }


        ConverterUtils.checkGetIsvalidOrNor(getVaccinationSlotRequest.getDate());
        Timestamp startDate = ConverterUtils.getStartDate(getVaccinationSlotRequest.getDate());
        Timestamp endDate = ConverterUtils.getEndDate(getVaccinationSlotRequest.getDate());

//        DocterDetiails docterDetiails = doctorRepositry.findByDoctorId("11");
        List<BookVacinationAppDetiails> bookVacinationAppDetiails = vacinationBookRepositry.findByDoctorIdAndAppointmentDateGreaterThanAndAppointmentDateLessThan(getVaccinationSlotRequest.getDocId(), startDate, endDate);
        List<VacinationSalotingDetiails> vacinationSalotingDetiails = vacinationSlotingRepositry.findAll();


        for (Integer i = 0; i < vacinationSalotingDetiails.size(); i++) {
            VacinationSalotingDetiails slotData = vacinationSalotingDetiails.get(i);

            if (bookVacinationAppDetiails.contains(slotData.getSlottingTime())) {
                slotData.setSlottingStatus("1");
                slotList.add(slotData);
            } else {
                slotData.setSlottingStatus("0");
                slotList.add(slotData);
            }
        }

        response.setSlotList(slotList);
        response.setMsg("Record Fetch Successfully");

        return ResponseUtils.createSuccessResponse(response, new TypeReference<VaccinationSlotResponse>() {
        });
    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod1(MultipartFile pet1, String formId) throws IOException {

        String mainFilePath = "data/mcpets/formSupprotDoc/" + formId + "/";

        if (pet1 == null || pet1.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Vaccination Book can not be blank.");
        }
        if (formId == null || formId.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form ID can not be blank.");
        }


        MainForm getMainFormData = mainFromRepositry.findByFormId(formId);
        if (getMainFormData == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid form Id/NA/1Q/" + formId);
        }


        MainDocForm saveData = mainDocFromRepositry.findByFormId(formId);
        if (saveData == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid doc form Id/NA/1Q/" + formId);
        }


        String fileExtension1 = ConverterUtils.getFileExtension(pet1);
        if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")
        ) {
        } else {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
        }
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


        saveData.setUpload_valid_book(path1.toString());
        mainDocFromRepositry.save(saveData);


        UplaodMainFormDocumentsResponse uplaodDocuments = new UplaodMainFormDocumentsResponse();
        uplaodDocuments.setMeassge("File upload successfully");
        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }

    @Override
    public ApiResponse<List<ReSchedulingVaccinationResponse>> getPendingVaccinationData() {
        List<ReSchedulingVaccinationResponse> reSchedulingVaccinationResponseList = new ArrayList<ReSchedulingVaccinationResponse>();

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.MOBILE_NO));

        List<BookVacinationAppDetiails> vacinationList = vacinationBookRepositry.findByMobileNumberAndIsComplete(currentLoggedInUser.get(HeaderUtils.MOBILE_NO), "0");


        for (Integer i = 0; i < vacinationList.size(); i++) {
            ReSchedulingVaccinationResponse reSchedulingVaccinationResponse = new ReSchedulingVaccinationResponse();
            BookVacinationAppDetiails bookVacinationAppDetiails = vacinationList.get(i);

            reSchedulingVaccinationResponse.setBookVacinationAppDetiails(bookVacinationAppDetiails);
        }

        return ResponseUtils.createSuccessResponse(reSchedulingVaccinationResponseList, new TypeReference<List<ReSchedulingVaccinationResponse>>() {
        });
    }

    @Override
    public ApiResponse<UplaodMainFormDocumentsResponse> reScheduleVaccinationAppointment(AppointmentRescduleRequest appointmentRequest) {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("id" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        UplaodMainFormDocumentsResponse docterListResponse = new UplaodMainFormDocumentsResponse();

        if (appointmentRequest.getFormId() == null || appointmentRequest.getFormId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Form Id can not be blank.");
        }
        if (appointmentRequest.getDoctorId() == null || appointmentRequest.getDoctorId().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Doctor Id can not be blank.");
        }
        if (appointmentRequest.getAppointmentDate() == null || appointmentRequest.getAppointmentDate().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Appointment Date can not be blank.");
        }
        if (appointmentRequest.getMobileNumber() == null || appointmentRequest.getMobileNumber().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Mobile Number can not be blank.");
        }
        if (appointmentRequest.getSlottingtime() == null || appointmentRequest.getSlottingtime().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Slotting time can not be blank.");
        }


        BookVacinationAppDetiails bookVacinationAppDetiails = vacinationBookRepositry.findByAppointmentId(appointmentRequest.getAppointmentId());
        if (bookVacinationAppDetiails == null) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Appointment Id");
        }


        ConverterUtils.checkGetIsvalidOrNor(appointmentRequest.getAppointmentDate());


        bookVacinationAppDetiails.setDoctorId(appointmentRequest.getDoctorId());
        bookVacinationAppDetiails.setAppointmentDate(ConverterUtils.convertDateTotimeStampBookAppointment(appointmentRequest.getAppointmentDate()));
        bookVacinationAppDetiails.setAppointmentCompleteDate(ConverterUtils.convertDateTotimeStampBookAppointment(appointmentRequest.getAppointmentDate()));
        bookVacinationAppDetiails.setFormId(appointmentRequest.getFormId());
        bookVacinationAppDetiails.setMobileNumber(appointmentRequest.getMobileNumber());
        bookVacinationAppDetiails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        bookVacinationAppDetiails.setIsCancel("0");
        bookVacinationAppDetiails.setIsComplete("0");
        bookVacinationAppDetiails.setSlottingTime(appointmentRequest.getSlottingtime());
        bookVacinationAppDetiails.setUserCode(ConverterUtils.createdOtp());

        appointmentRepositry.save(bookVacinationAppDetiails);
        bookVaccination(appointmentRequest.getFormId(), appointmentRequest.getMobileNumber(), appointmentRequest.getAppointmentDate() + " " + appointmentRequest.getSlottingtime());

        docterListResponse.setMeassge("Appointment ReSchedule Successfully");
        return ResponseUtils.createSuccessResponse(docterListResponse, new TypeReference<UplaodMainFormDocumentsResponse>() {
        });
    }

    private String bookVaccination(String formNumber, String mobileNo, String apponitmentDataTime) {

        HttpResponse response = null;
        String otp = ConverterUtils.createdOtp();
        String bookVaccination = "Dear Applicant, Your application no " + formNumber + " is scheduled for Vaccination on " + apponitmentDataTime + ". Please keep your documents ready on visit.Regards Team NOIDA";
        String url = "https://api.mylogin.co.in/api/v2/SendSMS";
        RestTemplate template = new RestTemplate();

        // Query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("ApiKey", HeaderUtils.NewAPIKEY)
                .queryParam("ClientId", HeaderUtils.NewClintId)
                .queryParam("SenderId", HeaderUtils.NewSENDER)
                .queryParam("Message", bookVaccination)
                .queryParam("MobileNumbers", "91" + mobileNo);

        System.out.println(builder.buildAndExpand().toUri());

        String responseNew = template.getForObject(builder.buildAndExpand().toUri(), String.class);
        System.out.println(responseNew);
        return otp;
    }

}


