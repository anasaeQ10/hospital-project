package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AjouterRendezvousFrame extends JFrame {
    private JTextField tfNomPatient, tfPrenomPatient, tfMotif;
    private JDateChooser dateChooser;
    private JComboBox<String> cbMaladie;  // ComboBox pour choisir la maladie
    private JTextField tfNomMedecin, tfPrenomMedecin;  // Affichage des informations du médecin automatiquement
    private JButton btnAjouter;

    public AjouterRendezvousFrame() {
        setTitle("Ajouter un Rendez-vous");
        setSize(400, 400); // Taille ajustée pour plus d'espace
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

        // Champs pour le patient
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Nom Patient :"), gbc);
        gbc.gridx++;
        tfNomPatient = new JTextField(20);
        inputPanel.add(tfNomPatient, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prénom Patient :"), gbc);
        gbc.gridx++;
        tfPrenomPatient = new JTextField(20);
        inputPanel.add(tfPrenomPatient, gbc);

        // Sélecteur de la date
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Date du Rendez-vous :"), gbc);
        gbc.gridx++;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        inputPanel.add(dateChooser, gbc);

        // Champ pour le motif
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Motif :"), gbc);
        gbc.gridx++;
        tfMotif = new JTextField(20);
        inputPanel.add(tfMotif, gbc);

        // Liste déroulante pour choisir la maladie
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Maladie :"), gbc);
        gbc.gridx++;
        cbMaladie = new JComboBox<>(new String[]{"Sélectionner", "Cardiopathie", "Diabète", "Orthopédie"});
        inputPanel.add(cbMaladie, gbc);

        // Champs pour afficher le médecin (attribué automatiquement)
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Médecin (Attribué) :"), gbc);
        gbc.gridx++;
        tfNomMedecin = new JTextField(20);
        tfNomMedecin.setEditable(false); // Non modifiable
        inputPanel.add(tfNomMedecin, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prénom Médecin (Attribué) :"), gbc);
        gbc.gridx++;
        tfPrenomMedecin = new JTextField(20);
        tfPrenomMedecin.setEditable(false); // Non modifiable
        inputPanel.add(tfPrenomMedecin, gbc);

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
                ajouterRendezvous();
            }
        });

        // Ajouter un écouteur d'événement pour la sélection de la maladie
        cbMaladie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignerMedecinParMaladie();
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void assignerMedecinParMaladie() {
    String maladie = (String) cbMaladie.getSelectedItem();
    if (maladie != null && !maladie.equals("Sélectionner")) {
        // En fonction de la maladie, déterminer la spécialité du médecin
        String specialite = "";
        if (maladie.equals("Cardiopathie")) {
            specialite = "Cardiologue";
        } else if (maladie.equals("Diabète")) {
            specialite = "Endocrinologue";
        } else if (maladie.equals("Orthopédie")) {
            specialite = "Orthopédiste";
        }

        if (!specialite.isEmpty()) {
            // Récupérer tous les médecins ayant cette spécialité
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

                // Requête pour récupérer tous les médecins ayant cette spécialité
                String sql = "SELECT nom, prenom FROM medecins WHERE specialite = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, specialite);

                ResultSet rs = stmt.executeQuery();

                // Stocker les médecins dans une liste
                List<String> medecins = new ArrayList<>();
                while (rs.next()) {
                    String nomMedecin = rs.getString("nom");
                    String prenomMedecin = rs.getString("prenom");
                    medecins.add(nomMedecin + " " + prenomMedecin);
                }

                // Vérifier s'il y a des médecins pour cette spécialité
                if (!medecins.isEmpty()) {
                    // Choisir un médecin aléatoirement
                    Random rand = new Random();
                    int index = rand.nextInt(medecins.size());
                    String medecinChoisi = medecins.get(index);
                    String[] nomPrenom = medecinChoisi.split(" "); // Séparer le nom et le prénom

                    // Mettre à jour les champs du médecin
                    tfNomMedecin.setText(nomPrenom[0]);
                    tfPrenomMedecin.setText(nomPrenom[1]);
                } else {
                    // Si aucun médecin n'est trouvé pour cette spécialité
                    JOptionPane.showMessageDialog(this, "Aucun médecin trouvé pour cette spécialité.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des médecins : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    private void ajouterRendezvous() {
        String nomPatient = tfNomPatient.getText();
        String prenomPatient = tfPrenomPatient.getText();
        java.util.Date dateRendezvousDate = dateChooser.getDate();
        String motif = tfMotif.getText();
        String nomMedecin = tfNomMedecin.getText();
        String prenomMedecin = tfPrenomMedecin.getText();

        if (dateRendezvousDate == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date sqlDateRendezvous = new java.sql.Date(dateRendezvousDate.getTime());

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Vérifier si le patient existe déjà
            String checkPatientSQL = "SELECT id FROM patients WHERE nom = ? AND prenom = ?";
            PreparedStatement checkPatientStmt = conn.prepareStatement(checkPatientSQL);
            checkPatientStmt.setString(1, nomPatient);
            checkPatientStmt.setString(2, prenomPatient);
            ResultSet rsPatient = checkPatientStmt.executeQuery();

            if (rsPatient.next()) {
                int patientId = rsPatient.getInt("id");

                // Vérifier si le médecin existe
                String checkMedecinSQL = "SELECT id FROM medecins WHERE nom = ? AND prenom = ?";
                PreparedStatement checkMedecinStmt = conn.prepareStatement(checkMedecinSQL);
                checkMedecinStmt.setString(1, nomMedecin);
                checkMedecinStmt.setString(2, prenomMedecin);
                ResultSet rsMedecin = checkMedecinStmt.executeQuery();

                if (rsMedecin.next()) {
                    int medecinId = rsMedecin.getInt("id");

                    // Ajouter le rendez-vous
                    String sql = "INSERT INTO rendez_vous (patient_id, date_rendez_vous, motif, medecin_id) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, patientId);
                    statement.setDate(2, sqlDateRendezvous);
                    statement.setString(3, motif);
                    statement.setInt(4, medecinId);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Rendez-vous ajouté avec succès !");
                        dispose(); // Fermer la fenêtre après l'ajout
                    }

                    statement.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Le médecin n'existe pas !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                rsMedecin.close();
                checkMedecinStmt.close();
            } else {
                JOptionPane.showMessageDialog(this, "Le patient n'existe pas !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            rsPatient.close();
            checkPatientStmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AjouterRendezvousFrame().setVisible(true));
    }
}
