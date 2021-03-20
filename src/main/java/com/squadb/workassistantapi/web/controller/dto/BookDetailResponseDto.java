package com.squadb.workassistantapi.web.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.squadb.workassistantapi.domain.Book;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookDetailResponseDto {

    private long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private int stockQuantity;
    private String description;
    private LocalDateTime publishingDate;
    private LocalDateTime registrationDate;

    public static BookDetailResponseDto of(Book book) {
        BookDetailResponseDto bookDetailResponseDto = new BookDetailResponseDto();
        bookDetailResponseDto.id = book.getId();
        bookDetailResponseDto.isbn = book.getIsbn();
        bookDetailResponseDto.title = book.getTitle();
        bookDetailResponseDto.author = book.getAuthor();
        bookDetailResponseDto.publisher = book.getPublisher();
        bookDetailResponseDto.imageUrl = book.getImageUrl();
        bookDetailResponseDto.stockQuantity = book.getStockQuantity();
        bookDetailResponseDto.description = book.getDescription();
        bookDetailResponseDto.publishingDate = book.getPublishingDate();
        bookDetailResponseDto.registrationDate = book.getRegistrationDate();
        return bookDetailResponseDto;
    }
}
