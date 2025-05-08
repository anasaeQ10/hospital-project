package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class modifierrendezvousFrame extends JFrame {
    private JTextField tfID, tfPatientID, tfDate, tfMotif, tfMedecinID;
    private JButton btnModifier;
    private int idRendezvous;

    public modifierrendezvousFrame(int idRendezvous) {
        setTitle("Modifier Rendez-vous");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        this.idRendezvous = idRendezvous;

        // Créer un panneau pour les champs d'entrée
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Champs de saisie pour les informations du rendez-vous
        inputPanel.add(new JLabel("ID :"), gbc);
        gbc.gridx++;
        tfID = new JTextField(String.valueOf(idRendezvous));
        tfID.setEditable(false);
        inputPanel.add(tfID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Patient ID :"), gbc);
        gbc.gridx++;
        tfPatientID = new JTextField();
        inputPanel.add(tfPatientID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Date (YYYY-MM-DD) :"), gbc);
        gbc.gridx++;
        tfDate = new JTextField();
        inputPanel.add(tfDate, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Motif :"), gbc);
        gbc.gridx++;
        tfMotif = new JTextField();
        inputPanel.add(tfMotif, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Médecin ID :"), gbc);
        gbc.gridx++;
        tfMedecinID = new JTextField();
        inputPanel.add(tfMedecinID, gbc);

        // Bouton de modification
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(50, 150, 250));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(btnModifier);

        // Charger les données du rendez-vous
        chargerDonneesRendezvous();

        // Action lors du clic sur le bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierRendezvous();
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Charger les informations du rendez-vous depuis la base de données
    private void chargerDonneesRendezvous() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String query = "SELECT * FROM rendez_vous WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idRendezvous);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfPatientID.setText(rs.getString("patient_id"));
                tfDate.setText(rs.getString("date_rendez_vous"));
                tfMotif.setText(rs.getString("motif"));
                tfMedecinID.setText(rs.getString("medecin_id"));
            } else {
                JOptionPane.showMessageDialog(this, "Rendez-vous non trouvé !");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données."+e.getMessage());
        }
    }

    // Modifier le rendez-vous dans la base de données
    private void modifierRendezvous() {
        String patientID = tfPatientID.getText();
        String date = tfDate.getText();
        String motif = tfMotif.getText();
        String medecinID = tfMedecinID.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String query = "UPDATE rendez_vous SET patient_id = ?, date_rendez_vous = ?, motif = ?, medecin_id = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientID);
            stmt.setString(2, date);
            stmt.setString(3, motif);
            stmt.setString(4, medecinID);
            stmt.setInt(5, idRendezvous);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Rendez-vous modifié avec succès.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du rendez-vous.");
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String idString = JOptionPane.showInputDialog("Entrez l'ID du rendez-vous à modifier :");
        if (idString != null && !idString.isEmpty()) {
            try {
                int id = Integer.parseInt(idString);
                SwingUtilities.invokeLater(() -> new modifierrendezvousFrame(id).setVisible(true));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "L'ID doit être un nombre entier.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID valide.");
        }
    }
}
