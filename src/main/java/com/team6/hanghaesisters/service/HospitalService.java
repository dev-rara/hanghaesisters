package com.team6.hanghaesisters.service;

import com.team6.hanghaesisters.entity.Hospital;
import com.team6.hanghaesisters.exception.CustomException;
import com.team6.hanghaesisters.exception.ErrorCode;
import com.team6.hanghaesisters.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URLEncoder;

@Service
@Slf4j
@RequiredArgsConstructor
@ToString
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Value("${hospital-url}")
    private String hospitalUrl;

    @Value("${hospital-key}")
    private String hospitalKey;

    private  Hospital getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        if (nValue == null)
            return null;
        return new Hospital(nValue.getNodeValue());
    }

    public void saveHospitalApiData(){
        try{
            StringBuilder urlBuilder = new StringBuilder(hospitalUrl); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "="+hospitalKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("QD", "UTF-8") + "=" + URLEncoder.encode("D010", "UTF-8")); /*CODE_MST의'D000' 참조(D001~D029)*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("4728", "UTF-8")); /*목록 건수*/
            String url = urlBuilder.toString();

            Document documentInfo = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url);

            documentInfo.getDocumentElement().normalize();

            NodeList nodeList = documentInfo.getElementsByTagName("item");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    Hospital hospitalData = getTagValue("dutyName", element);
                    log.info(":::" + hospitalData + ":::");
                    hospitalRepository.save(hospitalData);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            log.error("hospital data not saved");
            throw new CustomException(ErrorCode.FAILED_SAVE_DATA);
        }
    }
}
