/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Michailangelo
 * Created: Feb 25, 2019
 */

CREATE TABLE Genre(
  GenreID      INT    NOT NULL       PRIMARY KEY ,
  Name          VARCHAR(20)   NOT NULL	
);

CREATE TABLE FavoriteList(
FavoriteListID INT NOT NULL PRIMARY KEY,
Name VARCHAR(50) NOT NULL
);

CREATE TABLE Movie (
  MovieID      INT     NOT NULL       PRIMARY KEY ,
  Title         VARCHAR(100)    NOT NUll,
  GenreID INT  NOT NULL REFERENCES Genre(GenreID),
  ReleaseDate  DATE NOT NULL,
  Rating FLOAT(10) ,
  Overview  VARCHAR(500),
  FavoriteListID INT REFERENCES FavoriteList(FavoriteListID)
   
);


