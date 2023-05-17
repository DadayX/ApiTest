package com.siret.companyInfo.Service;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CustomTrustManager implements X509TrustManager {
     @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        // Do nothing or perform client certificate validation if needed
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        // Perform server certificate validation here
        // You can customize the validation logic based on your requirements
        // For example, you can compare the received certificates with the server's expected certificate
        // If validation fails, throw a CertificateException
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0]; // or null if you want to use the system default trust store
    }

}

