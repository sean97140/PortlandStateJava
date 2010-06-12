package edu.pdx.cs399J.di;

import java.io.File;

/**
 * A main program that launches a book store using the "production" {@link edu.pdx.cs399J.di.BookInventory} and
 * {@link edu.pdx.cs399J.di.CreditCardService}
 */
public class BookStoreApp
{
   public static void main(String... args) {
       String tmpdir = System.getProperty( "java.io.tmpdir" );
       File directory = new File( tmpdir );
       BookInventory inventory = new BookDatabase( directory, "books.txt" );
       CreditCardService cardService = new FirstBankOfPSU( "localhost", 8080 );
       BookStore store = new BookStore(inventory, cardService);
       
       // We don't really do much here...
   }
}