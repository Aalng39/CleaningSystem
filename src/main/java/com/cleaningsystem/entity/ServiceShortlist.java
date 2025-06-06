package com.cleaningsystem.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cleaningsystem.entity.ServiceShortlist;

import static com.cleaningsystem.db.Queries.*;

import java.sql.ResultSet;
import java.util.List;

@Component
public class ServiceShortlist {

    private int shortlistId;
    private int homeownerId;
    private int serviceId;

    // No-args constructor
    public ServiceShortlist() {}

    public ServiceShortlist(int homeownerId, int serviceId) {
        this.homeownerId = homeownerId;
        this.serviceId = serviceId;
    }

    public int getShortlistId(){ return shortlistId; }
    public int getHomeownerId(){ return homeownerId; }
    public int getServiceId(){ return serviceId; }

    public void setShortlistId(int new_shortlistId){this.shortlistId = new_shortlistId;}
    public void setHomeownerId(int new_homeownerId){this.homeownerId = new_homeownerId;}
    public void setServiceId(int new_serviceId){this.serviceId = new_serviceId;}

    // Databases Stuff
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ServiceShortlist> listingRowMapper = (ResultSet rs, int rowNum) -> {
        ServiceShortlist serviceShortlists = new ServiceShortlist();
        // shortlist.setShortlistId(rs.getInt("shortlistId"));
        serviceShortlists.setHomeownerId(rs.getInt("homeownerId"));
        serviceShortlists.setServiceId(rs.getInt("serviceId"));
        return serviceShortlists;
    };

    public boolean shortlistService(int homeownerId, int serviceId) {
        int rows_affected = jdbcTemplate.update(SAVE_SHORTLISTED_SERVICE, 
            homeownerId, serviceId);

        return rows_affected >= 0;
    }

    public List<ServiceShortlist> searchShortlistedService(int homeownerId, String keyword) {
        String pattern = "%" + keyword + "%";
        return jdbcTemplate.query(SEARCH_SHORTLISTED_SERVICE_BY_NAME, listingRowMapper, homeownerId, pattern);
    }

    public List<ServiceShortlist> viewShortlistedService(int homeownerId) {
        return jdbcTemplate.query(GET_ALL_SHORTLISTED_SERVICES, listingRowMapper, homeownerId);
    }

    public List<ServiceShortlist> getNumberofViews(int cleanerId){
        return jdbcTemplate.query(GET_NO_OF_VIEWS, listingRowMapper, cleanerId);
    }

    public boolean checkShortlistedServices(int serviceId, int homeowner_Id) {
        List<ServiceShortlist> shortlists = jdbcTemplate.query(CHECK_SHORTLISTED_SERVICES, listingRowMapper, serviceId,homeowner_Id);
        return !shortlists.isEmpty();
    }
    public boolean deleteShortlistedServices(int homeownerId, int serviceId) {
        return jdbcTemplate.update(DELETE_SHORTLISTED_SERVICES, homeownerId, serviceId) >= 0;
    }

    public boolean deleteShortlistedServicesByCategory(int categoryId) {
        return jdbcTemplate.update(DELETE_SHORTLISTED_SERVICES_BY_CATEGORY, categoryId) >= 0;
    }
}