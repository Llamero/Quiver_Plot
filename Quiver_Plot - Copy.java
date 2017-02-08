/** Quiver_Plot.java:
 
 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 @author Ben Smith (benjamin.smith@berkeley.edu)
 @created January 1, 2017
 
*/


import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Plot;
import ij.plugin.PlugIn;
import ij.plugin.frame.Recorder;
import ij.process.ImageProcessor;
import javax.swing.*;

//Start the plugin

/**
 *
 * @author Ben
 */
public class Quiver_Plot implements PlugIn {
   

    //Override ensures that if I extend a class, that the refence is spelled correctly (i.e. I am not making a new class)

    /**
     *
     * @param arg
     */
    @Override

    //Initialize a GUI to allow for importing the names of the two windows
    //NOTE: GUI was modified from recorded GUI construction in NetBeans using JFrame Form
    public void run(String arg){
        //Initialize a panel (needed to house a frame)
        final JPanel guiPanel = new JPanel();

        //Initlialize a frame to hold the gui
        final JFrame guiFrame = new JFrame();

        //Set the frame to close when the window is closed
        guiFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //Get a list of all active image titles to be used in dropdown menus (combo boxes)
        String[] imageListArray = ij.WindowManager.getImageTitles();

        //See if any arguments have been passed to the plugin
        if (IJ.isMacro() && ij.Macro.getOptions() != null && !ij.Macro.getOptions().trim().isEmpty()){
            String[] arguments = ij.Macro.getOptions().split(",");

            //remove all spaces from arrat components
            for(int a = 0; a<arguments.length; a++){
                arguments[a] = arguments[a].trim();
            }

            //Make sure there are only three arguments, and that the arguments are valid
            if(arguments.length == 3){
                for(int a = 0; a<imageListArray.length; a++){
                    //Use the .equals comparator to test strings
                    if(arguments[0].equals(imageListArray[a])){
                        for(int b = 0; b<imageListArray.length; b++){
                           if(arguments[1].equals(imageListArray[b])){
                               if(arguments[2].matches("^\\d+$")){
                                   a = imageListArray.length;
                                   b = imageListArray.length;

                                   //Convert the third argument to an integer
                                   int resolution = Integer.parseInt(arguments[2]);

                                   //If the arguments are valid, send them directly to the plot generator, otherwise create  the user interface
                                   guiOkButtonActionPerformed(arguments[0], arguments[1], resolution);
                               }
                           }
                        }
                    } 
                }
            }
        }

        //Otherwise, create a GUI
        else{

            //First, declare the components of the user interface
            //Final means that the varibles will always refer to the same objects as when initialized
            final JLabel guiTitle = new javax.swing.JLabel();
            final JLabel guiIntro1 = new javax.swing.JLabel();
            final JLabel guiIntro2 = new javax.swing.JLabel();
            final JLabel guiIntro3 = new javax.swing.JLabel();
            final JLabel guiAngleText = new javax.swing.JLabel();
            final JComboBox guiAngleBox = new javax.swing.JComboBox();
            final JLabel guiSpeedText = new javax.swing.JLabel();
            final JComboBox guiSpeedBox = new javax.swing.JComboBox();
            final JLabel guiResText = new javax.swing.JLabel();
            final JTextField guiResBox = new javax.swing.JTextField();
            final JButton guiOkButton = new javax.swing.JButton();

            //Create the title text for the GUI
            guiTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
            guiTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            guiTitle.setText("Quiver Plot Generator");

            //Create the instruction text
            guiIntro1.setText("This plugin generates the corresponding quiver plot from two images:");
            guiIntro2.setText("1) An image where each pixel is the vector angle in degrees.");
            guiIntro3.setText("2) An image where each pixel is the vector displacement (relative length).");

            //Prompt users to select the corresponding images via dropdown menu containing all open images
            guiAngleText.setText("Angle Image:");
            guiAngleBox.setModel(new javax.swing.DefaultComboBoxModel(imageListArray));
            guiSpeedText.setText("Displacement Image:");
            guiSpeedBox.setModel(new javax.swing.DefaultComboBoxModel(imageListArray));

            //Prompt user to choose the resolution of the quiver plot
            guiResText.setText("Plot Resolution:");
            guiResBox.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            guiResBox.setText("512");

            //Create an OK button so that the plot can then be generated
            guiOkButton.setText("OK");
            guiOkButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    //Retrieve the values selected in the GUI
                    String angleImage = (String)guiAngleBox.getSelectedItem();
                    String speedImage = (String)guiSpeedBox.getSelectedItem();
                    int plotResolution = Integer.parseInt(guiResBox.getText());

                    //Remove the GUI frame now that it is no longer needed
                    guiFrame.dispose();

                    //If the Recorder is recording, pass the arguments to the recorder
                    if (Recorder.record){
                        String Command  = "run(\"Quiver Plot\",\"" + angleImage + ", " + speedImage + ", " + plotResolution+"\");\r\n";
                        Recorder.recordString(Command);
                    }

                    guiOkButtonActionPerformed(angleImage, speedImage, plotResolution);
                }
            });

            //Build the GUI using the aboe described parts.
            //The content pane is the layer of the frame that the GUI is built onto
            //NOTE: The actual formatting code was from the JFrame Form.  It was then modified to accept the guiFrame frame.
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(guiFrame.getContentPane());
            guiFrame.getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(guiTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(guiSpeedText)
                                .addComponent(guiAngleText))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(guiAngleBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(guiSpeedBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(guiIntro1)
                                .addComponent(guiIntro2)
                                .addComponent(guiIntro3)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(guiResText)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(guiResBox, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(guiOkButton)))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(guiTitle)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(guiIntro1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(guiIntro2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(guiIntro3)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(guiAngleText)
                        .addComponent(guiAngleBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(guiSpeedText)
                        .addComponent(guiSpeedBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(29, 29, 29)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(guiResText)
                        .addComponent(guiResBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guiOkButton)
                    .addContainerGap())
            );

            //Use the Pack function to make the GUI frame shrink to the smallest size necassary
            guiFrame.pack();

            //Show the GUI
            guiFrame.setVisible(true);

        }
    }
    
    //When okay button is clicked, get the angle and velocity data, and calcualte the X and Y displacement of each vector
    public void guiOkButtonActionPerformed(String angleImage, String speedImage, int resolution) {  
       
       //Initialize image processors for both of the selected images 
       ImagePlus anglePlus = WindowManager.getImage(angleImage);
       ImageProcessor angleProc = anglePlus.getChannelProcessor();
       ImagePlus speedPlus = WindowManager.getImage(speedImage);
       ImageProcessor speedProc = speedPlus.getChannelProcessor();
       
       //Extract the pixel values from the images into arrays
       float[] speedArray = (float[])speedProc.getPixels();
       float[] angleArray = (float[])angleProc.getPixels();
       
       //Find the fastest vector to scale velocities to
       float maxSpeed = 0;
       for(int a = 0; a<speedArray.length; a++){
           if(speedArray[a] > maxSpeed) maxSpeed = speedArray[a];
       }
       
       //Create a scaled speed array based on the max speed, plot resolution, and map resolution (such that max vector length is width of one grid square in plot)
       float[] scaledSpeedArray =  new float[speedArray.length];
       for(int a = 0; a<speedArray.length; a++) scaledSpeedArray[a] = speedArray[a] / maxSpeed;
       
       //Create an array of the x and y displacement of each array
       float[] xArray =  new float[speedArray.length];
       float[] yArray =  new float[speedArray.length];
       
       //Calculate the x and y scaled displacement of each vector
       for(int a = 0; a<speedArray.length; a++){
           xArray[a] = (float) (scaledSpeedArray[a]*Math.cos(Math.toRadians(angleArray[a])));
           yArray[a] = (float) (scaledSpeedArray[a]*Math.sin(Math.toRadians(angleArray[a])));
       }
       
       quiverplot(xArray, yArray, speedProc.getWidth(), speedProc.getHeight(), resolution);

       
       
    } 
    
    //Create a quiver plot using ImageJ
    private void quiverplot(float[] xArray, float[] yArray, int originalWidth, int originalHeight, int resolution){
        //Create a new plot in imageJ
        Plot quiver = new Plot("Quiver Plot","","");
        
        //Set the frame to the desired resolution
        quiver.setFrameSize(resolution, resolution);
        
        //Set the axes to the scale of the original image
        quiver.setLimits(1,originalWidth,1,originalHeight);
        
        //Create a 2D array to store the X1, Y1, X2, Y2 points for each vector
        double x1Array[] = new double[originalWidth*originalHeight];
        double y1Array[] = new double[originalWidth*originalHeight];
        double x2Array[] = new double[originalWidth*originalHeight];
        double y2Array[] = new double[originalWidth*originalHeight];
        
        //Calculate the four vector points ofr each vector
        for(int y = 0; y<originalHeight; y++){
            for(int x = 0; x<originalWidth; x++){
                int index = y*originalHeight + x;
                x1Array[index] = x+1;
                y1Array[index] = originalHeight-y; //Needed to make plot (y-up) match image (y-down)
                x2Array[index] = x+1+xArray[index];
                y2Array[index] = originalHeight-y+yArray[index]; //Needed to make plot (y-up) match image (y-down)
            }
        }
        
        quiver.drawVectors(x1Array, y1Array, x2Array, y2Array);
        quiver.show();
        
    }
}       
                
                


