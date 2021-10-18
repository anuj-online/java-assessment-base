package com.group9.assessment.javaassessmentbase.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Row {

  @JacksonXmlProperty(isAttribute = true, localName = "Id")
  private String id;

  @JacksonXmlProperty(isAttribute = true, localName = "PostTypeId")
  private String PostTypeId;

  @JacksonXmlProperty(isAttribute = true, localName = "AcceptedAnswerId")
  private int acceptedAnswerId;

  @JacksonXmlProperty(isAttribute = true, localName = "CreationDate")
  private LocalDateTime creationDate;

  @JacksonXmlProperty(isAttribute = true, localName = "ParentId")
  private int parentId;

  @JacksonXmlProperty(isAttribute = true, localName = "Score")
  private int score;

  @JacksonXmlProperty(isAttribute = true, localName = "Body")
  private String body;

  @JacksonXmlProperty(isAttribute = true, localName = "ViewCount")
  private int viewCount;

  @JacksonXmlProperty(isAttribute = true, localName = "OwnerUserId")
  private String ownerUserId;

  @JacksonXmlProperty(isAttribute = true, localName = "LastActivityDate")
  private LocalDateTime lastActivityDate;

  @JacksonXmlProperty(isAttribute = true, localName = "Title")
  private String title;

  @JacksonXmlProperty(isAttribute = true, localName = "CommentCount")
  private int commentCount;
}
