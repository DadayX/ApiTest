package com.siret.companyInfo.Service;

import com.siret.companyInfo.Entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.*;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class CompanyImpl implements CompanyService{
    @Autowired
    RestTemplate restTemplate;
    @Override
    public List<Company> getInfo(String id) {
        if(id.isEmpty()){
            return null;
        }
        String uri="https://api.insee.fr/entreprises/sirene/V3/siret/"+id,token="8778d17a-8c6e-3de3-8558-3ee603621c72",jsonBody="";

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity <String> entity=new HttpEntity<String>(httpHeaders);
        try {
            X509Certificate[] serverCertificates = retrieveServerCertificates(uri);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new CustomTrustManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> res =restTemplate.exchange(uri, HttpMethod.GET,entity,String.class);

            if(res.getStatusCode().value()!=200){
                System.out.println("["+new Date()+"]"+"[ERROR] - Status != 200");
                return null;
            }
            jsonBody=res.getBody();
            if(jsonBody.isEmpty()){
                return null;
            }
            List<Company> l=new ArrayList<>();
            System.out.println("JSONBODY:"+jsonBody);
            l.add(readJsonData(jsonBody));
            return l;
        } catch (Exception e) {
            System.out.println("["+new Date()+"]"+"[ERROR] - Certificat SSL Error"+e.getMessage()+"---"+e.getCause());
            e.getStackTrace();
            return null;
        }
    }
    public Company readJsonData(String jsonBody){
        Company c=new Company();
        JSONObject jsonObject=new JSONObject(jsonBody).getJSONObject("etablissement");
        c.setSiret(jsonObject.getString("siret"));
        c.setNic(jsonObject.getString("nic"));
        c.setCreation_date(jsonObject.getString("dateCreationEtablissement"));
        String full_address=jsonObject.getJSONObject("adresseEtablissement").getString("typeVoieEtablissement")+" "+
                jsonObject.getJSONObject("adresseEtablissement").getString("libelleVoieEtablissement")+" "+
                jsonObject.getJSONObject("adresseEtablissement").getString("codePostalEtablissement")+" "+
                jsonObject.getJSONObject("adresseEtablissement").getString("libelleCommuneEtablissement");
        c.setFull_address(full_address);
        c.setFull_name(jsonObject.getJSONObject("uniteLegale").getString("denominationUniteLegale"));
        return c;
    }
    @Override
    public String generateCSV(List<Company> companyData) {
        if(companyData.isEmpty()){
            return null;
        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyymmddHHMMss");
        String newDate=dateFormat.format(new Date());
        String filename="siretExtract_"+newDate+".csv";
        try {
            BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(filename));
            String header="",line ="";
            header="Siret;Nic;Full address;Creation date;Full name\n";
            bo.write(header.getBytes());
            bo.flush();
            for(Company data:companyData){
                line=data.getSiret()+";"+
                        data.getNic()+";"+
                        data.getFull_address()+";"+
                        data.getCreation_date()+";"+
                        data.getFull_name();
                bo.write(line.getBytes());
                bo.flush();
                line=null;
            }
            bo.close();
        }catch(IOException ex1){
            System.out.println("["+new Date()+"]"+"[IOExeption]"+ ex1.getMessage() +"==>"+ex1.getCause());
            return null;
        }catch (Exception ex2){
            System.out.println("["+new Date()+"]"+"[Other Exeption]"+ ex2.getMessage() +"==>"+ex2.getCause());
            return null;
        }


        return filename;
    }

    public ResponseEntity<Object> getResponseEntity(List<Company> lc) {String filename=generateCSV(lc);
        File file = new File(filename);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
             System.out.println("["+new Date()+"]"+"[IOExeption]"+ e.getMessage() +"==>"+e.getCause());
        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyymmddHHMMss");
        String newDate=dateFormat.format(new Date());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object>
            responseEntity = ResponseEntity.ok().headers(headers).contentLength(
            file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
    return responseEntity;
    }
    public  X509Certificate[] retrieveServerCertificates(String uri) {
        try{
            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection)  url.openConnection();
            connection.connect();
            Certificate[] certificates = connection.getServerCertificates();
            X509Certificate[] serverCertificates = new X509Certificate[certificates.length];
            for (int i = 0; i < certificates.length; i++) {
                serverCertificates[i] = (X509Certificate) certificates[i];
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new CustomTrustManager()}, null);
            sslContext.getSocketFactory().createSocket().close(); // Force the initialization of the TrustManager
            System.out.println("["+new Date()+"]"+"[INFO] SSL OKK");
            return serverCertificates;
        }catch (Exception e){
            e.getStackTrace();
            return null;
        }
    }

}
