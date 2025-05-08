package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AjouterPatientFrame extends JFrame {
    private JTextField tfNom, tfPrenom, tfDateNaissance, tfMaladies, tfTelephone, tfEmail;
    private JButton btnAjouter;

    public AjouterPatientFrame() {
        setTitle("Ajouter un Patient");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panneau pour les champs d'entrée
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx++;
        tfNom = new JTextField(20);
        inputPanel.add(tfNom, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prénom :"), gbc);
        gbc.gridx++;
        tfPrenom = new JTextField(20);
        inputPanel.add(tfPrenom, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Date de Naissance :"), gbc);
        gbc.gridx++;
        tfDateNaissance = new JTextField("YYYY-MM-DD", 20);
        inputPanel.add(tfDateNaissance, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Maladies :"), gbc);
        gbc.gridx++;
        tfMaladies = new JTextField(20);
        inputPanel.add(tfMaladies, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx++;
        tfTelephone = new JTextField(20);
        inputPanel.add(tfTelephone, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx++;
        tfEmail = new JTextField(20);
        inputPanel.add(tfEmail, gbc);

        // Bouton Ajouter
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(50, 150, 250));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(btnAjouter);

        // Action pour le bouton Ajouter
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterPatient();
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void ajouterPatient() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String dateNaissance = tfDateNaissance.getText();
        String maladies = tfMaladies.getText();
        String telephone = tfTelephone.getText();
        String email = tfEmail.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "INSERT INTO patients (nom, prenom, date_naissance, maladies, telephone, email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, dateNaissance);
            statement.setString(4, maladies);
            statement.setString(5, telephone);
            statement.setString(6, email);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Patient ajouté avec succès !");
                dispose(); // Fermer la fenêtre après l'ajout
            }

            statement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AjouterPatientFrame().setVisible(true));
    }
}
