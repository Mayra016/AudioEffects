package main.java.Controllers;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.*;

import javax.sound.sampled.*;

import javazoom.jl.player.advanced.AdvancedPlayer;


@SuppressWarnings("serial")
public class ScreenController extends JFrame implements LineListener {
	private JButton STOP;
	private Thread playerThread;
	private AdvancedPlayer player;
	boolean isJar = false;
	private InputStream inputStream;
	
	public ScreenController() {
		// Initial window configuration
		super("Audio Effects");
		setTitle("Audio Effects");
	    setSize(800, 800);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    
	    // Style
	    UIManager.put("Button.background", Color.YELLOW);

	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); 
        
        int gridX = 0;
        int gridY = 0;
        int maxButtonsPerRow = 8;

	    
	    Map<String, String> soundFiles = listFiles("/main/resources/sounds/");
        Map<String, String> imageFiles = listFiles("/main/resources/images/");

	    
        // Create each soud effect button
        for (String baseName : soundFiles.keySet()) {
            String[] title = baseName.split("-");

            JButton button = createButton(title[1].trim(), imageFiles.get(baseName), soundFiles.get(baseName));
            
            gbc.gridx = gridX;
            gbc.gridy = gridY;
            
            panel.add(button, gbc);
            
            gridX++;
            
            if (gridX >= maxButtonsPerRow) {
            	gridX = 0;
            	gridY++;
            }
        }
        
        STOP = new JButton("STOP");
        
        
        STOP.addActionListener(new ActionListener() {
			
            @Override
            public void actionPerformed(ActionEvent e) {
            	stopSound();
            }
        });

        gbc.gridx = gridX;
        gbc.gridy = gridY;
        panel.add(STOP, gbc); 
        add(panel);
        setLayout(new FlowLayout());
        setVisible(true);
        
        
	    
	}

	// Iterate over the audio effect and image folder to load the media content
	private Map<String, String> listFiles(String dirPath) {
        Map<String, String> filesMap = new HashMap<>();
        try {
            URL resource = getClass().getResource(dirPath);
            if (resource != null) {
                if (resource.getProtocol().equals("jar")) {
                	isJar = true;
                    JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
                    JarFile jarFile = jarConnection.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String entryName = entry.getName();
                        if (entryName.startsWith(dirPath.substring(1)) && !entry.isDirectory()) {
                            String fileName = entryName.substring(entryName.lastIndexOf('/') + 1);
                            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                            filesMap.put(baseName, entryName);
                        }
                    }
                } else {
                    File directory = new File(resource.toURI());
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                String fileName = file.getName();
                                String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                                filesMap.put(baseName, file.getPath());
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	    return filesMap;
	}
	

	private JButton createButton(String title, String imgSource, String audioSource) {
		
		JButton btn = new JButton(title);
				
		try {
			if (imgSource != null) {
				
				 ImageIcon originalIcon = new ImageIcon(imgSource);
		         Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		         ImageIcon scaledIcon = new ImageIcon(scaledImage);
		         btn.setIcon(scaledIcon);
			}
			
		} catch (Exception e) {
			
		}
		
		btn.addActionListener(new ActionListener() {
			
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound(audioSource);
            }
        });
		
		
		return btn;
	}
	
	private void playSound(String soundSource) {
		try {
			
			
			if (isJar) {
				inputStream = getClass().getResourceAsStream("/" + soundSource);
			} else {
				inputStream = new FileInputStream(soundSource);
			}
			
            
			player = new AdvancedPlayer(inputStream);
			STOP.setEnabled(true);
			playerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        player.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        STOP.setEnabled(false);
            		    try {
            		        if (inputStream != null) {
            		            inputStream.close();
            		        }
            		    } catch (IOException e) {
            		        e.printStackTrace();
            		    }
            		    
            		    if (player != null) {
            		        player.close();
            		    }
            		    
            		    if (playerThread != null && playerThread.isAlive()) {
            		        playerThread.stop();
            		    }
            		    STOP.setEnabled(false);
                    }
                }
            });
            playerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void stopSound() {
        if (playerThread != null) {
        	player.close();
        	playerThread.stop();
        }
    }

	@Override
	public void update(LineEvent event) {
		// TODO Auto-generated method stub
		
	}

}
