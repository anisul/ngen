package com.grasshopper.ngen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.grasshopper.ngen.domain.enumeration.RestaurantType;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "verified")
    private Boolean verified;
    
    @Column(name = "banner_file")
    private String bannerFile;
    
    @Column(name = "about")
    private String about;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private RestaurantType category;
    
    @Column(name = "gmap_code")
    private String gmapCode;
    
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private Set<Deals> dealss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getVerified() {
        return verified;
    }
    
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getBannerFile() {
        return bannerFile;
    }
    
    public void setBannerFile(String bannerFile) {
        this.bannerFile = bannerFile;
    }

    public String getAbout() {
        return about;
    }
    
    public void setAbout(String about) {
        this.about = about;
    }

    public RestaurantType getCategory() {
        return category;
    }
    
    public void setCategory(RestaurantType category) {
        this.category = category;
    }

    public String getGmapCode() {
        return gmapCode;
    }
    
    public void setGmapCode(String gmapCode) {
        this.gmapCode = gmapCode;
    }

    public Set<Deals> getDealss() {
        return dealss;
    }

    public void setDealss(Set<Deals> dealss) {
        this.dealss = dealss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Restaurant restaurant = (Restaurant) o;
        if(restaurant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, restaurant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", phone='" + phone + "'" +
            ", verified='" + verified + "'" +
            ", bannerFile='" + bannerFile + "'" +
            ", about='" + about + "'" +
            ", category='" + category + "'" +
            ", gmapCode='" + gmapCode + "'" +
            '}';
    }
}
