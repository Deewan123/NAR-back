package com.sdd.service.Impl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.*;
import com.sdd.controller.WebApi.EmailSenderService;
import com.sdd.entities.*;
import com.sdd.entities.repository.CodeFromCategoryRepositry;
import com.sdd.entities.repository.DoctorRepositry;
import com.sdd.entities.repository.MainFromRepositry;
import com.sdd.entities.repository.VacinationBookRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.request.EmailRequest;
import com.sdd.request.PaymentRequest;
import com.sdd.response.*;
import com.sdd.service.ReportServices;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
//@SpringBootApplication
public class ReportServiceImpl  implements ReportServices {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private HeaderUtils headerUtils;
    //@Value("${file.dir}")
    //private String folderPath;

    //private String folderPath= "/src/main/resources";

    @Autowired
    VacinationBookRepositry vacinationBookRepositry;

    @Autowired
    DoctorRepositry doctorRepositry;
    @Autowired
    MainFromRepositry mainFromRepositry;

    @Autowired
    CodeFromCategoryRepositry codeFromCategoryRepositry;
    @Autowired
    private EmailSenderService senderService;

    @Override
    public ApiResponse<List<MailResponce>> emailRecept(EmailRequest req) throws ParseException, JSONException, DocumentException, IOException {
        List<MailResponce> responce = new ArrayList<MailResponce>();
        MailResponce rep = new MailResponce();
        sendMail(req.getEmail(),req.getUrl());
        rep.setMsg("success");
        responce.add(rep);
        return ResponseUtils.createSuccessResponse(responce, new TypeReference<List<MailResponce>>() {
        });
    }
    // @EventListener(ApplicationReadyEvent.class)/// send email automatocally when the app is start
    public void sendMail(String email,String url) {
        senderService.sendEmail(email,
                "NAPR Payment Acknowledgement",
                "Dear Customer,\n" +
                        "\n" +
                        "I am glad to report to you that I have just received the payment through bank service. Please find the receiving slip attached to this url. Timely payment will prove to be a good incentive...."+
                        "\n" +"                  "+
                        "\n" +url+
                        "\n" +"Regards"+ "\n" +"NAPR Team");
    }

    public ApiResponse<MailResponce>  receptData(PaymentRequest req) throws FileNotFoundException {
        MainForm mainForms = mainFromRepositry.findMainForsmByFormId(req.getFormId());
        MailResponce responce = new MailResponce();
        if (mainForms.getFormId() != null && !mainForms.getFormId().equalsIgnoreCase("")
                && !mainForms.getFormId().equalsIgnoreCase("null")) {
//for LOCAL
           // String folderPath = "/Users/manjuyadav/Desktop/ABCD/";
            //String folderPath = "C://Program Files/Apache Software Foundation/Tomcat 9.0/webapps/data/";
             String folderPath = HelperUtils.UPLOADFILEPATH;

            String fileName = "patmentrecept_" + req.getFormId() + ".pdf";

            //String currentDir = System.getProperty("user.dir");
            //System.out.println("Current dir using System:" + currentDir);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfWriter pdfWriter;
            try {
                pdfWriter = new PdfWriter(os);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument);
                pdfDocument.setDefaultPageSize(PageSize.A5);
                // Heading Table
                float colWidthForHeading[] = {600};
                Table tableForHeading = new Table(colWidthForHeading);

                tableForHeading.addCell(new Cell().add("NAPR").setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER).setFontSize(30f).setFontColor(Color.GRAY).setBold());
                tableForHeading.addCell(new Cell().add("Payment Recept").setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER).setFontSize(15f).setFontColor(Color.DARK_GRAY).setBold());

                // Heading Table Ends
                document.add(tableForHeading);
                // Third Table
                float columnWidthForBuyerName[] = {350, 210};
                Table tableforBuyerName = new Table(columnWidthForBuyerName);
                tableforBuyerName.setFontColor(Color.DARK_GRAY);

                tableforBuyerName.addCell(new Cell().add("Owner Name:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getOwnerName())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("Pet Name:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getNickName())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("owner Number:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getOwnerNumber())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("Payment Status:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                if (mainForms.getPaymentStatus() != null &&
                        !mainForms.getPaymentStatus().equalsIgnoreCase("")
                        && !mainForms.getPaymentStatus().equalsIgnoreCase("null")) {
                    //rep.setPaymentStatus("Approved");
                    tableforBuyerName.addCell(new Cell().add("Approved")
                            .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                            .setBorder(Border.NO_BORDER));
                } else {
                    //rep.setPaymentStatus("Pending");
                    tableforBuyerName.addCell(new Cell().add("Pending")
                            .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                            .setBorder(Border.NO_BORDER));
                }
                tableforBuyerName.addCell(new Cell().add("Amount:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getAmount())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("Dog DOB:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getDob())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("Gender:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getSex())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("Address:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getAddress())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));

                tableforBuyerName.addCell(new Cell().add("PinCode:")
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                tableforBuyerName.addCell(new Cell().add("" + mainForms.getPincode())
                        .setMarginTop(5f).setMarginBottom(5f).setFontSize(10f).setMarginLeft(5f)
                        .setBorder(Border.NO_BORDER));
                document.add(tableforBuyerName);
                // Third Table Ends

                // Close the document
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] b = os.toByteArray();
            //String tempPath= currentDir+"/"+folderPath+fileName;
            String tempPath = folderPath + fileName;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(tempPath);
                os.writeTo(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        return ApiResponse.builder()
//                .data(path)
//                .message("Success")
//                .build();
            responce.setMsg(HelperUtils.DOWNLOADFILE + fileName);
            return ResponseUtils.createSuccessResponse(responce, new TypeReference<MailResponce>() {
            });
        }else{
            responce.setMsg("File Not Found");
            return ResponseUtils.createFailureResponse(responce, new TypeReference<MailResponce>() {
            }, "No data Found", HttpStatus.REQUEST_TIMEOUT.value());
        }
    }

//    public String generateUUIDFileName(String fileName) {
//        UUID uuid = UUID.randomUUID();
//        String str = fileName;
//        System.out.println(str);
//        str = uuid.toString() + "." + str.substring(str.lastIndexOf(".") + 1);
////	        str = uuid.toString() + "." + str.substring(str.lastIndexOf(".") + 1);
//        System.out.println(str);
//        return str;
//    }













}
