/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Alfabeto;
import Negocio.Controlador;
import Negocio.DAOtxt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import comunicacion.DTO;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kathe
 */
public class GUI extends javax.swing.JFrame {

    private Controlador controlador;
    private ArrayList<Alfabeto> alfabetos;
    
    DefaultListModel<String> modelMetodos, modelArchivos;
    
    public GUI() {
        modelMetodos = new DefaultListModel();
        modelArchivos = new DefaultListModel();
		
        lstMetodos = new javax.swing.JList<>(modelMetodos);
        lstArchivos = new javax.swing.JList<>(modelArchivos);

        initComponents();
        txtTextoInicial.requestFocus();
        
        try{
            controlador = new Controlador();
        
            consultarAlgoritmos();
            consultarArchivos();
            consultarAlfabetos();
            
        }catch(NullPointerException ex){
            JOptionPane.showMessageDialog(this, 
                        "No se pudo establecer conexión con el servidor",
                        "Error", JOptionPane.ERROR_MESSAGE);
            
            btnAceptar.setEnabled(false);
            btnEstablecerDefault.setEnabled(false);
            chkUsarDefault.setEnabled(false);
        }
    }
    
    private void consultarAlfabetos(){
        alfabetos = controlador.consultarAlfabetos();
        
        if(alfabetos == null){
            JOptionPane.showMessageDialog(this, "Error al cargar alfabetos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            cbxAlfabeto.removeAllItems();
            for(Alfabeto a : alfabetos){
                cbxAlfabeto.addItem(a.getId());
            }
            
            txtElementos.setText(alfabetos.get(0).getElementos());
        }
    }
    
    private void refrescarElementos(){
        int seleccionado = cbxAlfabeto.getSelectedIndex();
        if(seleccionado != -1)
            txtElementos.setText(alfabetos.get(seleccionado).getElementos());
    }
    
    private void consultarAlgoritmos(){
        ArrayList<String> nombres = controlador.consultarAlgoritmos();
        for(String s : nombres){
            cmbxMetodos.addItem(s);
        }
    }
    
    private void consultarArchivos(){
        ArrayList<String> nombres = controlador.consultarArchivosSalida();
        for(String s : nombres){
            cmbxArchivos.addItem(s);
        }
    }
    
    private void anadirMetodo(){
        String metodoSeleccionado = String.valueOf(cmbxMetodos.getSelectedItem());
        if(!modelMetodos.contains(metodoSeleccionado)){
            modelMetodos.addElement(metodoSeleccionado);
        }
    }
    
    private void quitarMetodo(){
        int metodoSeleccionado = lstMetodos.getSelectedIndex();
        if(metodoSeleccionado != -1){
            modelMetodos.remove(metodoSeleccionado);
        }
    }
    
    private void anadirArchivo(){
        String metodoSeleccionado = String.valueOf(cmbxArchivos.getSelectedItem());
        if(!modelArchivos.contains(metodoSeleccionado)){
            modelArchivos.addElement(metodoSeleccionado);
        }
    }
    
    private void quitarArchivo(){
        int metodoSeleccionado = lstArchivos.getSelectedIndex();
        if(metodoSeleccionado != -1){
            modelArchivos.remove(metodoSeleccionado);
        }
    }
    
    private ArrayList<Integer> ingresarAlgoritmos(){
        ArrayList<String> algoritmos = controlador.consultarAlgoritmos();
        ArrayList<Integer> algoritmosSeleccionados = new ArrayList<>();
        for(int i=0; i<algoritmos.size(); i++){
            if(modelMetodos.contains(algoritmos.get(i))){
                algoritmosSeleccionados.add(i);
            }
        }
        
        return algoritmosSeleccionados;
    }
    
    private ArrayList<String> ingresarTiposArchivos(){
        ArrayList<String> archivosSeleccionados = new ArrayList<>();
        for(int i=0; i<modelArchivos.size(); i++){
            archivosSeleccionados.add(modelArchivos.get(i));
        }
        return archivosSeleccionados;
    }
    
    private void ejecutarOperacion(){        
        String textoInicial = txtTextoInicial.getText();
        if(!textoInicial.isEmpty()){
            ArrayList<Integer> algoritmosSeleccionados = ingresarAlgoritmos();
            if(!algoritmosSeleccionados.isEmpty()){
                ArrayList<String> archivosSeleccionados = ingresarTiposArchivos();
                if(rbtnDesencriptar.isSelected() && algoritmosSeleccionados.size()>1){
                    JOptionPane.showMessageDialog(this, 
                        "Para desencriptación sólo puede utilizarse un método.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                    return;     //corto circuito
                }

                ArrayList<String> nombres = controlador.consultarAlgoritmos();

                DTO dto = new DTO();
                dto.setModo(rbtnEncriptar.isSelected());
                dto.setTextoInicial(txtTextoInicial.getText());
                dto.setAlgoritmosSeleccionados(algoritmosSeleccionados);
                dto.setNombresAlgoritmos(nombres);
                dto.setTiposArchivos(archivosSeleccionados);
                dto.setFechaHora(Calendar.getInstance());
                
                String nombreAlfabeto;
                if(chkUsarDefault.isSelected()){
                    nombreAlfabeto = cbxAlfabeto.getSelectedItem().toString();
                    nombreAlfabeto = nombreAlfabeto.replace("(default)", "");
                }else{
                    nombreAlfabeto = cbxAlfabeto.getSelectedItem().toString();
                }
                System.out.println(nombreAlfabeto);
                dto.setNombreAlfabeto(nombreAlfabeto);

                //Resultados
                ArrayList<String> resultados = controlador.operar(dto);

                if(resultados == null){
                    JOptionPane.showMessageDialog(this, "Hubo un error en el texto de entrada.\n"
                            + "Por favor intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    String resultado = "";
                    int posicion;
                    for(int i=0; i<algoritmosSeleccionados.size(); i++){
                        posicion = algoritmosSeleccionados.get(i);
                        resultado += nombres.get(posicion) + ":\n";
                        resultado += resultados.get(i) + "\n";
                    }
                    txtResultado.setText(resultado);
                }
            }else{
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un algoritmo de encriptación", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "El texto de entrada está vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void establecerAlfabetoDefault(){
        String nombre = cbxAlfabeto.getSelectedItem().toString();
        System.out.println(nombre);
        DTO dto = new DTO();
        dto.setNombreAlfabeto(nombre);
        
        if(controlador.establecerAlfabetoDefault(dto)){
            JOptionPane.showMessageDialog(this,
                        "Establecido por defecto!", "Aceptado", JOptionPane.INFORMATION_MESSAGE);
            
            consultarAlfabetos();   //actualiza los alfabetos
        }else{
            JOptionPane.showMessageDialog(this,
                        "Error al establecer por defecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void usarAlfabetoDefault(){
        if(chkUsarDefault.isSelected()){
            cbxAlfabeto.setEnabled(false);
            btnEstablecerDefault.setEnabled(false);
            for(int i=0; i<cbxAlfabeto.getItemCount(); i++){
                String item = cbxAlfabeto.getItemAt(i);
                if(item.contains("(default)")){
                    cbxAlfabeto.setSelectedIndex(i);
                    break;
                }
            }
        }else{
            cbxAlfabeto.setEnabled(true);
            btnEstablecerDefault.setEnabled(true);
            cbxAlfabeto.setSelectedIndex(0);
        }  
    }
    
    private void examinarArchivo(){
        // muestra el cuadro de diálogo de archivos, para que el usuario pueda elegir el archivo a abrir
        JFileChooser selectorArchivos = new JFileChooser();
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //filtra el tipo de archivo seleccionado
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
        selectorArchivos.setFileFilter(filtro);

        // indica cual fue la accion de usuario sobre el jfilechooser
        int resultado = selectorArchivos.showOpenDialog(this);

        File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado

        if ((archivo == null) || (archivo.getName().equals(""))) {
            JOptionPane.showMessageDialog(this, "Archivo inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            String texto = controlador.leerArchivo(archivo.getAbsolutePath());
            
            if(texto != null){
                txtTextoInicial.setText(texto);
            }else{
                JOptionPane.showMessageDialog(this, "Error al abrir archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }

    private void habilitarComponentes(boolean estado){
        btnGenerarFrase.setEnabled(!estado);
        btnExaminar.setEnabled(estado);
        txtTextoInicial.setEnabled(estado);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btngFrases = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTextoInicial = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResultado = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        cmbxMetodos = new javax.swing.JComboBox<>();
        cmbxArchivos = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        btnAnadirMetodo = new javax.swing.JButton();
        btnAnadirArchivos = new javax.swing.JButton();
        btnQuitarMetodo = new javax.swing.JButton();
        btnQuitarArchivos = new javax.swing.JButton();
        rbtnEncriptar = new javax.swing.JRadioButton();
        rbtnDesencriptar = new javax.swing.JRadioButton();
        cbxAlfabeto = new javax.swing.JComboBox<>();
        txtElementos = new javax.swing.JTextField();
        chkUsarDefault = new javax.swing.JCheckBox();
        btnEstablecerDefault = new javax.swing.JButton();
        rbtnConsDup = new javax.swing.JRadioButton();
        rbtnConsNoDup = new javax.swing.JRadioButton();
        rbtnNoConsNoDup = new javax.swing.JRadioButton();
        rbtnIngresar = new javax.swing.JRadioButton();
        btnExaminar = new javax.swing.JButton();
        btnGenerarFrase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Algoritmos");

        jLabel2.setText("Frase");

        jLabel3.setText("Tipo archivo");

        jLabel4.setText("Métodos");

        txtTextoInicial.setColumns(20);
        txtTextoInicial.setRows(1);
        txtTextoInicial.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(txtTextoInicial);

        jLabel5.setText("Modo");

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        jLabel6.setText("Resultado");

        txtResultado.setColumns(20);
        txtResultado.setRows(5);
        jScrollPane2.setViewportView(txtResultado);

        jLabel7.setText("Alfabeto");

        jScrollPane3.setViewportView(lstMetodos);

        jScrollPane4.setViewportView(lstArchivos);

        btnAnadirMetodo.setText("Añadir");
        btnAnadirMetodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirMetodoActionPerformed(evt);
            }
        });

        btnAnadirArchivos.setText("Añadir");
        btnAnadirArchivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirArchivosActionPerformed(evt);
            }
        });

        btnQuitarMetodo.setText("Quitar");
        btnQuitarMetodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarMetodoActionPerformed(evt);
            }
        });

        btnQuitarArchivos.setText("Quitar");
        btnQuitarArchivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarArchivosActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnEncriptar);
        rbtnEncriptar.setSelected(true);
        rbtnEncriptar.setText("Encriptar");

        buttonGroup1.add(rbtnDesencriptar);
        rbtnDesencriptar.setText("Desencriptar");

        cbxAlfabeto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxAlfabetoItemStateChanged(evt);
            }
        });

        txtElementos.setEnabled(false);

        chkUsarDefault.setText("Usar default");
        chkUsarDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUsarDefaultActionPerformed(evt);
            }
        });

        btnEstablecerDefault.setText("Establecer Default");
        btnEstablecerDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstablecerDefaultActionPerformed(evt);
            }
        });

        btngFrases.add(rbtnConsDup);
        rbtnConsDup.setText("Consecutivos duplicados");
        rbtnConsDup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnConsDupActionPerformed(evt);
            }
        });

        btngFrases.add(rbtnConsNoDup);
        rbtnConsNoDup.setText("Consecutivos no duplicados");
        rbtnConsNoDup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnConsNoDupActionPerformed(evt);
            }
        });

        btngFrases.add(rbtnNoConsNoDup);
        rbtnNoConsNoDup.setText("No consecutivos no duplicados");
        rbtnNoConsNoDup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnNoConsNoDupActionPerformed(evt);
            }
        });

        btngFrases.add(rbtnIngresar);
        rbtnIngresar.setSelected(true);
        rbtnIngresar.setText("Ingresar");
        rbtnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnIngresarActionPerformed(evt);
            }
        });

        btnExaminar.setText("Examinar");
        btnExaminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExaminarActionPerformed(evt);
            }
        });

        btnGenerarFrase.setText("Generar");
        btnGenerarFrase.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnQuitarMetodo)
                                .addGap(132, 132, 132)
                                .addComponent(btnQuitarArchivos))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4)
                                    .addComponent(cmbxMetodos, 0, 122, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAnadirMetodo)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3)
                                    .addComponent(cmbxArchivos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAnadirArchivos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rbtnDesencriptar)
                                            .addComponent(rbtnEncriptar)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(17, 17, 17)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(rbtnConsDup)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(rbtnConsNoDup)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(rbtnIngresar)
                                                    .addComponent(rbtnNoConsNoDup))))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnGenerarFrase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnExaminar, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtElementos, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(cbxAlfabeto, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(chkUsarDefault)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnEstablecerDefault)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(181, 181, 181)
                                .addComponent(btnAceptar))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(jLabel1)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxAlfabeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkUsarDefault)
                    .addComponent(btnEstablecerDefault))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtElementos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(rbtnConsDup)
                    .addComponent(rbtnNoConsNoDup)
                    .addComponent(btnGenerarFrase))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnIngresar)
                    .addComponent(rbtnConsNoDup))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnExaminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbxArchivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnAnadirArchivos)
                                .addComponent(rbtnEncriptar)))
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbxMetodos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAnadirMetodo))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuitarMetodo)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnQuitarArchivos)
                        .addComponent(rbtnDesencriptar)))
                .addGap(18, 18, 18)
                .addComponent(btnAceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        ejecutarOperacion();
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnAnadirMetodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirMetodoActionPerformed
        anadirMetodo();
    }//GEN-LAST:event_btnAnadirMetodoActionPerformed

    private void btnAnadirArchivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirArchivosActionPerformed
        anadirArchivo();
    }//GEN-LAST:event_btnAnadirArchivosActionPerformed

    private void btnQuitarMetodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarMetodoActionPerformed
        quitarMetodo();
    }//GEN-LAST:event_btnQuitarMetodoActionPerformed

    private void btnQuitarArchivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarArchivosActionPerformed
        quitarArchivo();
    }//GEN-LAST:event_btnQuitarArchivosActionPerformed

    private void cbxAlfabetoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxAlfabetoItemStateChanged
        refrescarElementos();
    }//GEN-LAST:event_cbxAlfabetoItemStateChanged

    private void chkUsarDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUsarDefaultActionPerformed
        usarAlfabetoDefault();
        if(chkUsarDefault.isSelected()){
            cbxAlfabeto.setEnabled(false);
            btnEstablecerDefault.setEnabled(false);
        }else{
            cbxAlfabeto.setEnabled(true);
            btnEstablecerDefault.setEnabled(true);
        }
    }//GEN-LAST:event_chkUsarDefaultActionPerformed

    private void btnEstablecerDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstablecerDefaultActionPerformed
        establecerAlfabetoDefault();
    }//GEN-LAST:event_btnEstablecerDefaultActionPerformed

    private void rbtnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnIngresarActionPerformed
        habilitarComponentes(true);
    }//GEN-LAST:event_rbtnIngresarActionPerformed

    private void rbtnConsNoDupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnConsNoDupActionPerformed
        habilitarComponentes(false);
    }//GEN-LAST:event_rbtnConsNoDupActionPerformed

    private void rbtnConsDupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnConsDupActionPerformed
        habilitarComponentes(false);
    }//GEN-LAST:event_rbtnConsDupActionPerformed

    private void rbtnNoConsNoDupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnNoConsNoDupActionPerformed
        habilitarComponentes(false);
    }//GEN-LAST:event_rbtnNoConsNoDupActionPerformed

    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExaminarActionPerformed
        examinarArchivo();
    }//GEN-LAST:event_btnExaminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnAnadirArchivos;
    private javax.swing.JButton btnAnadirMetodo;
    private javax.swing.JButton btnEstablecerDefault;
    private javax.swing.JButton btnExaminar;
    private javax.swing.JButton btnGenerarFrase;
    private javax.swing.JButton btnQuitarArchivos;
    private javax.swing.JButton btnQuitarMetodo;
    private javax.swing.ButtonGroup btngFrases;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxAlfabeto;
    private javax.swing.JCheckBox chkUsarDefault;
    private javax.swing.JComboBox<String> cmbxArchivos;
    private javax.swing.JComboBox<String> cmbxMetodos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> lstArchivos;
    private javax.swing.JList<String> lstMetodos;
    private javax.swing.JRadioButton rbtnConsDup;
    private javax.swing.JRadioButton rbtnConsNoDup;
    private javax.swing.JRadioButton rbtnDesencriptar;
    private javax.swing.JRadioButton rbtnEncriptar;
    private javax.swing.JRadioButton rbtnIngresar;
    private javax.swing.JRadioButton rbtnNoConsNoDup;
    private javax.swing.JTextField txtElementos;
    private javax.swing.JTextArea txtResultado;
    private javax.swing.JTextArea txtTextoInicial;
    // End of variables declaration//GEN-END:variables
}
