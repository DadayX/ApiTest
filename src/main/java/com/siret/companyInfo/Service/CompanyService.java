package com.siret.companyInfo.Service;

import com.siret.companyInfo.Entity.Company;
import java.util.List;
public interface CompanyService {
    public List<Company> getInfo(String id);
    public String generateCSV(List<Company> companyData);
}
