package com.educacionit.digitalers.blog.entities;

import java.awt.TrayIcon.MessageType;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ResponseMessage {
	private MessageType messageType;
	private String description;
}
