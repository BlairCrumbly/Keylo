package com.keylo.dto;

import java.time.LocalDateTime;

public class CredentialResponse {
    private Long id;
    private String siteName;
    private String siteUrl;
    private String loginEmail;
    private String loginUsername;
    private String loginPassword;
    private String description;
    private LocalDateTime createdAt;

    //! getters
    public Long getId() { return id; }
    public String getSiteName() { return siteName; }
    public String getSiteUrl() { return siteUrl; }
    public String getLoginEmail() { return loginEmail; }
    public String getLoginUsername() { return loginUsername; }
    public String getLoginPassword() { return loginPassword; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    //! setters
    public void setId(Long id) { this.id = id; }
    public void setSiteName(String siteName) { this.siteName = siteName; }
    public void setSiteUrl(String siteUrl) { this.siteUrl = siteUrl; }
    public void setLoginEmail(String loginEmail) { this.loginEmail = loginEmail; }
    public void setLoginUsername(String loginUsername) { this.loginUsername = loginUsername; }
    public void setLoginPassword(String loginPassword) { this.loginPassword = loginPassword; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
