package com.nexamart.modal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;

	private String description;

	private int mrpPrice;

	private int sellingPrice;

	private int discountPercent;

	private int quantity;

	private String colour;

	@ElementCollection // means for every image create new table
	private List<String> images = new ArrayList<>();

	private int numRating;

	@ManyToOne
	private Category category;

	@ManyToOne
	private Seller seller;

	private LocalDate createAt;

	// @ElementCollection
	private String size;
	
	@OneToMany(mappedBy = "product")
	private List<Review> reviews = new ArrayList<>();

}
