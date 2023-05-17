package com.siret.companyInfo.Controller;

import com.siret.companyInfo.Entity.Company;
import com.siret.companyInfo.Service.CompanyImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CompanyController {
    @RequestMapping(value = "companyInfo/siret/{id}",method = RequestMethod.GET)
    public ResponseEntity<Object> getSiretInfo(@PathVariable("id") String id){
        CompanyImpl c=new CompanyImpl();
        System.out.println("["+new Date()+"]"+"[ID]"+ id);
        List<Company> lc=new ArrayList<>();
        lc=c.getInfo(id);
        if(lc.isEmpty()||lc==null){
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("Status : NOK", HttpStatus.NOT_FOUND);
        }
        return  c.getResponseEntity(lc);
    }

}
