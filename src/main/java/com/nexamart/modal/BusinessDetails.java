package com.nexamart.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDetails {
	private String accountNumber;

	private String accountHolderName;

	private String ifscCode;

}
