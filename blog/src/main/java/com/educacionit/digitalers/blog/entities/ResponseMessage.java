package com.educacionit.digitalers.blog.entities;

import org.springframework.stereotype.Component;

import com.educacionit.digitalers.blog.enums.MessageType;

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
