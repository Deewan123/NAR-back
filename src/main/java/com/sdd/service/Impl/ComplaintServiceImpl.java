package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.*;
import com.sdd.entities.repository.*;
import com.sdd.exception.SDDException;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.request.CreateComplaintRequest;
import com.sdd.response.*;
import com.sdd.service.ComplaintServices;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ComplaintServiceImpl implements ComplaintServices {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    ComplaintRepositry complaintRepositry;


    @Autowired
    CodeAddressSectorRepositry codeAddressSectorRepositry;

    @Autowired
    CodeBreedTypeRepositry codeBreedTypeRepositry;

    @Autowired
    CodeFromCategoryRepositry codeFromCategoryRepositry;

    @Autowired
    CodeAddressVillageRepositry codeAddressVillageRepositry;

    @Autowired
    MangerScoietyRepositry mangerScoietyRepositry;

    @Autowired
    CustomerDetailsRepositry customerDetailsRepositry;


    @Override
    public ApiResponse<List<GetAllComplaintResponse>> getAllComplaint() {
        List<GetAllComplaintResponse> getAllComplaintResponses = new ArrayList<GetAllComplaintResponse>();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }
        List<Complaint> complaintsList = complaintRepositry.findAllByOrderByCreatedOnDesc();
        if (!CollectionUtils.isEmpty(complaintsList)) {
            complaintsList.forEach(complaitData -> {
                GetAllComplaintResponse getAllComplaintResponse = new GetAllComplaintResponse();
                getAllComplaintResponse.setComplaintId(complaitData.getComplaintId());
                getAllComplaintResponse.setOwnerName(complaitData.getOwnerName());
                getAllComplaintResponse.setReason(complaitData.getReason());
                getAllComplaintResponse.setCreatedOn(complaitData.getCreatedOn());
                getAllComplaintResponse.setStatus(complaitData.getStatus());
                getAllComplaintResponse.setDetailsOfComplainer(complaitData.getDetailsOfComplainer());
                getAllComplaintResponse.setNameOfComplainer(complaitData.getNameOfComplainer());
            });
        }
        return ResponseUtils.createSuccessResponse(getAllComplaintResponses, new TypeReference<List<GetAllComplaintResponse>>() {
        });

    }

    @Override
    public ApiResponse<List<GetAllComplaintResponse>> getAllPendingComplaint() {
        List<GetAllComplaintResponse> getAllComplaintResponses = new ArrayList<GetAllComplaintResponse>();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }
        List<Complaint> complaintsList = complaintRepositry.findByIsDoneOrderByCreatedOnDesc(0);
        if (!CollectionUtils.isEmpty(complaintsList)) {
            complaintsList.forEach(complaitData -> {
                GetAllComplaintResponse getAllComplaintResponse = new GetAllComplaintResponse();
                getAllComplaintResponse.setComplaintId(complaitData.getComplaintId());
                getAllComplaintResponse.setOwnerName(complaitData.getOwnerName());
                getAllComplaintResponse.setReason(complaitData.getReason());
                getAllComplaintResponse.setCreatedOn(complaitData.getCreatedOn());
                getAllComplaintResponse.setStatus(complaitData.getStatus());
                getAllComplaintResponse.setDetailsOfComplainer(complaitData.getDetailsOfComplainer());
                getAllComplaintResponse.setNameOfComplainer(complaitData.getNameOfComplainer());
            });
        }
        return ResponseUtils.createSuccessResponse(getAllComplaintResponses, new TypeReference<List<GetAllComplaintResponse>>() {
        });
    }

    @Override
    public ApiResponse<UpdateComplaintStatus> updateComplaintStatus(String complaintId) {
        UpdateComplaintStatus updateComplaintStatus = new UpdateComplaintStatus();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }
        Complaint complaint = complaintRepositry.findByComplaintId(complaintId);
        if(complaint == null){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID COMPLAINT ID");
        }
        complaint.setIsDone(1);
        complaintRepositry.save(complaint);
        updateComplaintStatus.setMessage("COMPLAINT STATUS CHANGED SUCCESSFULLY");

        return ResponseUtils.createSuccessResponse(updateComplaintStatus, new TypeReference<UpdateComplaintStatus>() {
        });
    }

    @Override
    public ApiResponse<List<GetAllComplaintResponse>> getAllCompleteComplaint() {
        List<GetAllComplaintResponse> getAllComplaintResponses = new ArrayList<GetAllComplaintResponse>();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }
        List<Complaint> complaintsList = complaintRepositry.findByIsDoneOrderByCreatedOnDesc(1);
        if (!CollectionUtils.isEmpty(complaintsList)) {
            complaintsList.forEach(complaitData -> {
                GetAllComplaintResponse getAllComplaintResponse = new GetAllComplaintResponse();
                getAllComplaintResponse.setComplaintId(complaitData.getComplaintId());
                getAllComplaintResponse.setOwnerName(complaitData.getOwnerName());
                getAllComplaintResponse.setReason(complaitData.getReason());
                getAllComplaintResponse.setCreatedOn(complaitData.getCreatedOn());
                getAllComplaintResponse.setStatus(complaitData.getStatus());
                getAllComplaintResponse.setDetailsOfComplainer(complaitData.getDetailsOfComplainer());
                getAllComplaintResponse.setNameOfComplainer(complaitData.getNameOfComplainer());
            });
        }
        return ResponseUtils.createSuccessResponse(getAllComplaintResponses, new TypeReference<List<GetAllComplaintResponse>>() {
        });
    }


    @Override
    public ApiResponse<List<Complaint>> getComplaint() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }
        List<Complaint> complaintsList = complaintRepositry.findByCompUserIdOrderByCreatedOnDesc(currentLoggedInUser.get(HeaderUtils.USER_ID));
        return ResponseUtils.createSuccessResponse(complaintsList, new TypeReference<List<Complaint>>() {
        });
    }

    @Override
    public ApiResponse<ComplaintResponse> createComplaint(CreateComplaintRequest createComplaintRequest) {
        ComplaintResponse complaintResponse = new ComplaintResponse();
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
        }
        if (createComplaintRequest.getDetailsOfComplainer() == null || createComplaintRequest.getDetailsOfComplainer().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "DETAILS COMPLAINER CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getPincode() == null || createComplaintRequest.getPincode().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "PIN CODE CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getAddressLine1() == null || createComplaintRequest.getAddressLine1().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "ADDRESS LINE CAN NOT BE BLANK");
        }


        if (createComplaintRequest.getAddressType() == null || createComplaintRequest.getAddressType().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "ADDRESS TYPE COMPLAINER CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getReason() == null || createComplaintRequest.getReason().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "RESASON CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getPetName() == null || createComplaintRequest.getPetName().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "PET NAME  CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getOwnerName() == null || createComplaintRequest.getOwnerName().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "OWNER NAME CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getPetType() == null || createComplaintRequest.getPetType().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "DETAILS COMPLAINER CAN NOT BE BLANK");
        }
        if (createComplaintRequest.getBreedType() == null || createComplaintRequest.getBreedType().isEmpty()) {
            throw new SDDException(HttpStatus.NO_CONTENT.value(), "DETAILS COMPLAINER CAN NOT BE BLANK");
        }

        if (createComplaintRequest.getComplainerName() == null || createComplaintRequest.getComplainerName().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "DETAILS COMPLAINER CAN NOT BE BLANK");
        }


        Complaint complaint = new Complaint();
        complaint.setComplaintId(HelperUtils.getComplaintId());
        complaint.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        complaint.setIsFlag(1);
        complaint.setIsDone(0);
        complaint.setStatus("PRO");
        complaint.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        complaint.setAddress(createComplaintRequest.getAddressLine1());
        complaint.setDetailsOfComplainer(createComplaintRequest.getDetailsOfComplainer());

        complaint.setBlock(createComplaintRequest.getBlock());
        complaint.setReason(createComplaintRequest.getReason());
        complaint.setAddLine1(createComplaintRequest.getAddressLine1());
        complaint.setAddressType(createComplaintRequest.getAddressType());

        complaint.setFlatNo(createComplaintRequest.getFlatNumber());
        complaint.setLandmark(createComplaintRequest.getLandmark());

        complaint.setSectorOther(createComplaintRequest.getSectorOther());
        complaint.setSocietyOther(createComplaintRequest.getSocietyOther());


        complaint.setNameOfComplainer(createComplaintRequest.getComplainerName());
        complaint.setOwnerName(createComplaintRequest.getOwnerName());
        complaint.setPincode(createComplaintRequest.getPincode());
        complaint.setPetName(createComplaintRequest.getPetName());



       CodeAddressSector codeAddressSector = codeAddressSectorRepositry.findBySectorId(createComplaintRequest.getSectorNumber());
       CodeAddressVillage codeAddressVillage = codeAddressVillageRepositry.findByVillageId(createComplaintRequest.getVillageId());
       MangerScoiety mangerScoiety = mangerScoietyRepositry.findByManageSocietyId(createComplaintRequest.getSocietyNumber());

       CodeBreedType codeBreedType = codeBreedTypeRepositry.findByBreedId(createComplaintRequest.getBreedId());
       CodeFromCategory codeFromCategory = codeFromCategoryRepositry.findByCatIdOrderByDescrAsc(createComplaintRequest.getCatId());
       CustomerDetail customerDetail = customerDetailsRepositry.findByCustomerId(currentLoggedInUser.get(HeaderUtils.USER_ID));

        complaint.setCatId(codeFromCategory);
        complaint.setBreedId(codeBreedType);
        complaint.setCompUserId(currentLoggedInUser.get(HeaderUtils.USER_ID));
        complaint.setManageSocietyId(mangerScoiety);
        complaint.setSectorId(codeAddressSector);
        complaint.setVillage_id(codeAddressVillage);
        complaint.setUserId(customerDetail);

        Complaint complaint1 = complaintRepositry.save(complaint);
        complaintResponse.setMessage("Complaint register successfully");
        complaintResponse.setComplaintId(complaint1.getComplaintId());

        return ResponseUtils.createSuccessResponse(complaintResponse, new TypeReference<ComplaintResponse>() {
        });
    }

    @Override
    public ApiResponse<UplaodDocuments> fileUplaod(MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4,String complaintId) throws IOException {

        String filePath ="data/mcpets/formSupprotDoc/dog/";

//        String token = headerUtils.getTokeFromHeader();
//        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
//        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
//        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
//        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
//            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
//        }
        if (file1 == null || file1.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Photo1 can not be blank.");
        }
        if (file2 == null || file2.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Photo2 can not be blank.");
        }
        if (file3 == null || file3.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Photo3 can not be blank.");
        }
        if (file4 == null || file4.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Photo4 can not be blank.");
        }

        String fileExtension1 = ConverterUtils.getFileExtension(file1);
        String fileExtension2 = ConverterUtils.getFileExtension(file2);
        String fileExtension3 = ConverterUtils.getFileExtension(file3);
        String fileExtension4 = ConverterUtils.getFileExtension(file4);
        if (fileExtension1.equalsIgnoreCase(".jpeg") || fileExtension1.equalsIgnoreCase(".png") || fileExtension1.equalsIgnoreCase(".jpg")) {

        } else {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support only image allow");
        }

        if (fileExtension2.equalsIgnoreCase(".jpeg") || fileExtension2.equalsIgnoreCase(".png") || fileExtension2.equalsIgnoreCase(".jpg")) {

        } else {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support only image allow");
        }

        if (fileExtension3.equalsIgnoreCase(".jpeg") || fileExtension3.equalsIgnoreCase(".png") || fileExtension3.equalsIgnoreCase(".jpg")) {

        } else {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support only image allow");
        }

        if (fileExtension4.equalsIgnoreCase(".jpeg") || fileExtension4.equalsIgnoreCase(".png") || fileExtension4.equalsIgnoreCase(".jpg")) {

        } else {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support only image allow");
        }


        if (complaintId == null || complaintId.isEmpty() ) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Complaint Id.");
        }

        Complaint  complaint = complaintRepositry.findByComplaintId(complaintId);
        if(complaint == null){
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Complaint Id.");
        }

        String filename1 = ConverterUtils.getRandomString(file1);
        String filename2 = ConverterUtils.getRandomString(file2);
        String filename3 = ConverterUtils.getRandomString(file3);
        String filename4 = ConverterUtils.getRandomString(file4);


        File targetFile1 = ConverterUtils.getTargetFile(fileExtension1, filename1,filePath);
        File targetFile2 = ConverterUtils.getTargetFile(fileExtension2, filename2,filePath);
        File targetFile3 = ConverterUtils.getTargetFile(fileExtension3, filename3,filePath);
        File targetFile4 = ConverterUtils.getTargetFile(fileExtension4, filename4,filePath);

        File mainPath1 = ConverterUtils.getComplaintPathOnly(fileExtension1, filename1,filePath);
        File mainPath2 = ConverterUtils.getComplaintPathOnly(fileExtension2, filename2,filePath);
        File mainPath3 = ConverterUtils.getComplaintPathOnly(fileExtension3, filename3,filePath);
        File mainPath4 = ConverterUtils.getComplaintPathOnly(fileExtension4, filename4,filePath);

        Path path1 = Paths.get(targetFile1.toString());
        Path path2 = Paths.get(targetFile2.toString());
        Path path3 = Paths.get(targetFile3.toString());
        Path path4 = Paths.get(targetFile4.toString());

        InputStream in1 = new ByteArrayInputStream(file1.getBytes());
        InputStream in2 = new ByteArrayInputStream(file2.getBytes());
        InputStream in3 = new ByteArrayInputStream(file3.getBytes());
        InputStream in4 = new ByteArrayInputStream(file4.getBytes());

        try {

            Files.copy(in1, path1.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(in2, path2.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(in3, path3.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(in4, path4.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);

        }
        catch (IOException e) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid File Path" +e);
        }

        UplaodDocuments uplaodDocuments =new UplaodDocuments();

        complaint.setPhotoUrl(mainPath1.toString());
        complaint.setPhotoUrl2(mainPath2.toString());
        complaint.setPhotoUrl3(mainPath3.toString());
        complaint.setPhotoUrl4(mainPath4.toString());
        complaintRepositry.save(complaint);

        uplaodDocuments.setPath("File Save Successfully");

        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodDocuments>() {
        });
    }


}


