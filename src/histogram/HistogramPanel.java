/*
 * University of Central Florida
 * COP3330 - Fall 2018
 * Author: Stephen Speer
 */
package histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class HistogramPanel extends JPanel {
	   
	   private static final long serialVersionUID = 1L;
	   private List<String> sents;
	   private int snum;
	   
	   public String readFile( File file ) {
	      sents = new ArrayList<>();
	      snum = -1;
	      clearDisplay( this.getGraphics() );
	      StringBuilder sb = new StringBuilder();
	      try {
	         Scanner scanner = new Scanner( new FileInputStream(file));
	         while( scanner.hasNextLine() ) {
	            String s = scanner.nextLine().trim();
	            if( s.length() > 0 ) {
	               sents.add( s );
	            }
	         }
	         scanner.close();
	         
	         for( int i = 0; i < sents.size(); i++ ) {
	            sb.append(i + " : " + sents.get( i ) + "\n\n");
	         }
	      } catch (FileNotFoundException ex) {
	         Logger.getLogger(HistogramPanel.class.getName()).log(Level.SEVERE, null, ex);
	      }
	      return sb.toString().trim();
	   }
	   
	   @Override
	   public void paintComponent( Graphics gc ) {
	      super.paintComponent( gc );
	      if( sents != null && snum >= 0 && snum < sents.size() ) { 
	         showHisto( snum, true );
	      }
	   }
	   
	   public void showHisto() {
	      this.setBackground( Color.white );
	      showHisto( snum, false );
	   }
	   
	   public void showHisto( int n, boolean b ) {        

	      if( sents != null && n >= 0 && n < sents.size() ) {
	         snum = n;
	         Graphics gc = this.getGraphics();
	         clearDisplay( gc );
	         drawLines( gc );
	         drawHisto( gc );
	      }
	      else if( b && sents != null ) {
	         JOptionPane.showMessageDialog(this, "Sentence index out of range");
	      }
	      
	   }
	   
	   private void clearDisplay( Graphics gc ) {      
	      gc.setColor( Color.WHITE );
	      gc.fillRect(0,0,this.getWidth(),this.getHeight());
	   }
	   
	   private void drawLines( Graphics gc ) { 
		   System.out.println("here");
		   gc.setColor(Color.red);
		  // gc.drawRect(100, 100, 60, 100);
	      
		// Draw Axis
		gc.drawLine((int) (getWidth() * 0.1) , (int) (getHeight() * 0.1), (int) (getWidth() * 0.1), (int) (getHeight() * 0.8));
		gc.drawLine((int) (getWidth() * 0.1), (int) (getHeight() * 0.8), (int) (getWidth() * 0.8), (int) (getHeight() * 0.8));

		   
	      
	   }
	   
	   private void drawHisto( Graphics gc ) {
		   
		   // grab the sentence we need
		   String str = sents.get(snum);
		   
		   // create buckets to put # of instance of characters
		   int[] bucket = new int[26];
		   
		   for(int i = 0; i < str.length(); i ++)
		   {
			   // ignore spaces
			   if( (int) str.charAt(i) == 32)
				   continue;
			   
			   // subtracting 'a' will give character values from 0-25
			   bucket[ (str.charAt(i) - 'a') ] ++;
		   }
		   
		   // width of each column
		   int width = (int) (getWidth() * 0.7 / 26);
		   
		   // find the max number of instances of character
		   int maxVal = 0;
		   for(Integer x : bucket)
			   if(x > maxVal)
				   maxVal = x;
		   
		   
		   System.out.println("max " + maxVal);
		   
		   // height will represent 1 instance of a char
		   int height = (int) ( (this.getHeight() * 0.7) / (maxVal));
		   
		   // coordinates of where to draw rectangles
		   // +- 2 pixels so axis is not overlapped
		   int xLoc = (int) (this.getWidth() * 0.1) + 2;
		   
		   // yLoc is at bottom of graph
		   int yLoc = (int) (this.getHeight() * 0.8) + - 2;
		   
		   gc.setColor(Color.blue);
		   
		   // loop through alphabet and draw each rectangle
		   for(int x = 0; x < 26; x ++)
		   {
			   // ignore empty buckets
			   if(bucket[x] != 0)
				   gc.drawRect(xLoc, yLoc - (bucket[x] * height) , width, height * bucket[x] );
			
			   // move along x-axis
			   xLoc += width;
		   }
		   
	      
	   }

	   private int[] scaledHisto( int[] inp ) { 
	      int[] histo = new int[ inp.length ];
	      int max = findMax( inp );
	      
	      double scaleFactor = 0.8 * this.getHeight();
	      for( int i = 0; i < histo.length; i++ ) {
	         double scaled =  scaleFactor * 
	               ( ((double) inp[ i ] ) / max );
	         histo[ i ] = (int) Math.floor(scaled);
	      }
	      return histo;
	   }
		   
	   private int[] rawHisto( String s ) {
	      s = s.toLowerCase();
	      int[] letter = new int[ 26 ];
	      for( int i = 0; i < s.length(); i++ ) {
	         char ch = s.charAt( i );
	         if( Character.isLetter(ch) ) {
	            int num = ch - 'a';
	            letter[ num ]++;
	         }
	      }
	      return letter;
	   }

	   private static int findMax( int[] inp ) {
	      int max = inp[ 0 ];
	      for( int i = 0; i < inp.length; i++ ) {
	         if( inp[ i ] > max ) {
	            max = inp[ i ];
	         }
	      }
	      return max;
	   }
	}

