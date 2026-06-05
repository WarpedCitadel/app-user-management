package com.warpedcitadel.appusermanagement.management;

import com.warpedcitadel.appusermanagement.management.model.SearchAttributesModel;
import com.warpedcitadel.appusermanagement.management.model.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {

    @Autowired
    private ManagementRepository managementRepository;

    protected Slice<UserDetailsModel> getAppUsers(Pageable pageable, SearchAttributesModel attributes) {
        return managementRepository.getAppUsers(pageable, attributes);
    }
}
