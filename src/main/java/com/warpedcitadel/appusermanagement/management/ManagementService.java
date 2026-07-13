package com.warpedcitadel.appusermanagement.management;

import com.warpedcitadel.appusermanagement.management.dto.GetAppUsersDto;
import com.warpedcitadel.appusermanagement.management.dto.SearchAttributesDto;
import com.warpedcitadel.appusermanagement.management.dto.SlicedResponse;
import com.warpedcitadel.appusermanagement.management.model.SearchAttributesModel;
import com.warpedcitadel.appusermanagement.management.model.UserDetailsModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagementService {

    private final ManagementRepository managementRepository;

    public ManagementService(ManagementRepository managementRepository) {
        this.managementRepository = managementRepository;
    }

    protected GetAppUsersDto getAppUsers(Pageable pageable, SearchAttributesDto attributesDto) {

        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        if (limit >= 51) {
            throw new IllegalArgumentException("Content requested too large");
        }

        SearchAttributesModel attributesModel =
                new SearchAttributesModel(
                        attributesDto.displayName(),
                        attributesDto.role(),
                        attributesDto.isActive()
                );

        List<Object> attributesList = new ArrayList<>();

        if (attributesModel.getDisplayName() != null &&
                !attributesDto.displayName().isEmpty()) {
            attributesList.add(attributesModel.getDisplayName().concat("%"));
        } else {
            attributesModel.setDisplayName("%");
            attributesList.add(attributesModel.getDisplayName());
        }
        if (attributesModel.getRole() != null &&
                !attributesModel.getRole().isEmpty()) {
            attributesList.add(attributesModel.getRole());
        } else {
            attributesModel.setRole(null);
            attributesList.add(attributesModel.getRole());
        }
        if (attributesModel.getIsActive() != null) {
            attributesList.add(attributesModel.getIsActive());
        } else {
            attributesModel.setIsActive(null);
            attributesList.add(attributesModel.getIsActive());
        }

        attributesList.add(limit + 1);
        attributesList.add(offset);

        Slice<UserDetailsModel> sliceUsers = managementRepository.getAppUsers(pageable, attributesList);
        SlicedResponse<UserDetailsModel> filterData = new SlicedResponse<>(sliceUsers);
        GetAppUsersDto data = new GetAppUsersDto(filterData);
        return data;
    }


    // Todo: add authorization logic
    public void enableAppUser(String uuid) {
        managementRepository.enableAppUser(uuid);
    }


    // Todo: add authorization logic
    public void disableAppUser(String uuid) {
        managementRepository.disableAppUser(uuid);
    }
}
