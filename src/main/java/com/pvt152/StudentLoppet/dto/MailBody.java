package com.pvt152.StudentLoppet.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}