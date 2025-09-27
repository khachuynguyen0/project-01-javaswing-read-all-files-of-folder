package com.filereader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Java Swing application to read all files from a selected folder
 * and display their contents in a GUI interface.
 */
public class FileReaderApp extends JFrame {
    
    private JButton selectFolderButton;
    private JTree fileTree;
    private JTextArea fileContentArea;
    private JScrollPane treeScrollPane;
    private JScrollPane contentScrollPane;
    private File selectedFolder;
    
    public FileReaderApp() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Java Swing File Reader - Read All Files of Folder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Initialize components
        selectFolderButton = new JButton("Select Folder");
        
        // Create empty tree initially
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("No folder selected");
        fileTree = new JTree(root);
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeScrollPane = new JScrollPane(fileTree);
        treeScrollPane.setPreferredSize(new Dimension(300, 400));
        
        // Text area for file contents
        fileContentArea = new JTextArea();
        fileContentArea.setEditable(false);
        fileContentArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        contentScrollPane = new JScrollPane(fileContentArea);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with button
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(selectFolderButton);
        add(topPanel, BorderLayout.NORTH);
        
        // Split pane for tree and content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeScrollPane);
        splitPane.setRightComponent(contentScrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Status bar
        JLabel statusLabel = new JLabel("Ready - Select a folder to browse files");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Folder selection button handler
        selectFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });
        
        // Tree selection handler
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (node != null && node.getUserObject() instanceof File) {
                    File file = (File) node.getUserObject();
                    if (file.isFile()) {
                        displayFileContent(file);
                    } else {
                        fileContentArea.setText("Selected: " + file.getName() + " (Directory)");
                    }
                }
            }
        });
    }
    
    private void selectFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select Folder to Read Files From");
        
        if (selectedFolder != null) {
            fileChooser.setCurrentDirectory(selectedFolder.getParentFile());
        }
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFolder = fileChooser.getSelectedFile();
            loadFilesFromFolder(selectedFolder);
        }
    }
    
    private void loadFilesFromFolder(File folder) {
        try {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(folder.getName());
            buildFileTree(root, folder);
            
            fileTree.setModel(new javax.swing.tree.DefaultTreeModel(root));
            
            // Expand the root node
            fileTree.expandRow(0);
            
            fileContentArea.setText("Folder loaded: " + folder.getAbsolutePath() + 
                                  "\nSelect a file from the tree to view its contents.");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading folder: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buildFileTree(DefaultMutableTreeNode parent, File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            // Sort files: directories first, then files
            java.util.Arrays.sort(files, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
            
            for (File file : files) {
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file);
                parent.add(fileNode);
                
                if (file.isDirectory()) {
                    buildFileTree(fileNode, file);
                }
            }
        }
    }
    
    private void displayFileContent(File file) {
        try {
            if (file.length() > 1024 * 1024) { // 1MB limit
                fileContentArea.setText("File too large to display (> 1MB): " + file.getName());
                return;
            }
            
            // Try to read as text file
            Path path = file.toPath();
            List<String> lines = Files.readAllLines(path);
            String content = lines.stream().collect(Collectors.joining("\n"));
            
            fileContentArea.setText("File: " + file.getName() + "\n" +
                                  "Path: " + file.getAbsolutePath() + "\n" +
                                  "Size: " + file.length() + " bytes\n" +
                                  "Last Modified: " + new java.util.Date(file.lastModified()) + "\n" +
                                  "----------------------------------------\n\n" +
                                  content);
            
            fileContentArea.setCaretPosition(0);
            
        } catch (IOException e) {
            fileContentArea.setText("Error reading file: " + file.getName() + "\n" +
                                  "Error: " + e.getMessage() + "\n\n" +
                                  "This file might be binary or in an unsupported encoding.");
        } catch (Exception e) {
            fileContentArea.setText("Unexpected error reading file: " + file.getName() + "\n" +
                                  "Error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system L&F not available
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileReaderApp().setVisible(true);
            }
        });
    }
}