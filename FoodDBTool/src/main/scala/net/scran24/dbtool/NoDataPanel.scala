/*
This file is part of Intake24.

© Crown copyright, 2012, 2013, 2014.

This software is licensed under the Open Government Licence 3.0:

http://www.nationalarchives.gov.uk/doc/open-government-licence/
*/

package net.scran24.dbtool

import javax.swing.JPanel
import java.awt.BorderLayout
import javax.swing.JLabel
import java.awt.Font
import javax.swing.SwingConstants
import javax.swing.BoxLayout
import java.awt.Color

class NoDataPanel extends JPanel {
  setLayout(new BorderLayout())
  
  val message = new JLabel("<html><p>Data location is not set or is not a valid Intake24 data directory.</p><p>Please set a valid data location using the Settings menu.</p></html>")
  message.setFont(new Font("Dialog", 0, 20))
  message.setHorizontalAlignment(SwingConstants.CENTER)
  
  add(message, BorderLayout.CENTER)
  
}