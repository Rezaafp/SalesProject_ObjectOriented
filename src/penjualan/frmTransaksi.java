/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package penjualan;

import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.*;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ASUS
 */
public class frmTransaksi extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsBrg;
    ResultSet RsKons;
    Statement stm;
    double total=0;
    String tanggal;
    Boolean edit=false;
    PreparedStatement pstat;
    String sKd_Kons;
    String sKd_Brg;
    String xnojual;
    
    DefaultTableModel tableModel = new DefaultTableModel(new Object [][]{}, new String [] {"Kd Barang", "Nama Barang","Harga Barang","Jumlah","Total"});
    /**
     * Creates new form frmTransaksi
     */
    public frmTransaksi() {
        initComponents();
        open_db();
        inisialisasi_tabel();
        aktif(false);
        setTombol(true);
        cmdCetak.setEnabled(false);
        txtTgl.setEditor(new JSpinner.DateEditor(txtTgl, "yyyy/MM/dd"));
    }
    
    private void setField(){
        int row=tblJual.getSelectedRow();
        cmbKd_Brg.setSelectedItem((String)tblJual.getValueAt(row,0));
        txtNm_Brg.setText((String)tblJual.getValueAt(row,1));
        String harga = Double.toString((Double)tblJual.getValueAt(row,2));
        txtHarga.setText(harga);
        String jumlah=Integer.toString((Integer)tblJual.getValueAt(row,3));
        txtJml.setText(jumlah);
        String total=Double.toString((Double)tblJual.getValueAt(row,4));
        txtTot.setText(total);
    }

    private void hitung_jual(){
        double xtot,xhrg;
        int xjml;
        xhrg=Double.parseDouble(txtHarga.getText());
        xjml=Integer.parseInt(txtJml.getText());
        xtot=xhrg*xjml;
        String xtotal=Double.toString(xtot);
        txtTot.setText(xtotal);
        total=total+xtot;
        txtTotal.setText(Double.toString(total));
    }
 
    private void open_db(){
        try{
            KoneksiMysql kon = new KoneksiMysql ("localhost","root","","penjualan");
            Con = kon.getConnection();
            //System.out.println("Berhasil ");
        }catch (Exception e) {
            System.out.println("Error : "+e);
        }
    } 
 
    private void baca_konsumen(){
        try{
            stm=Con.createStatement(); 
            pstat = Con.prepareStatement("select kd_kons,nm_kons from konsumen",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            rs.beforeFirst();
            while(rs.next()){
                cmbKd_Kons.addItem(rs.getString(1).trim());
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    }
    
    public void inisialisasi_tabel(){
        tblJual.setModel(tableModel);
    } 
 
    private void baca_barang(){
        try{
            stm=Con.createStatement(); 
 
            pstat = Con.prepareStatement("select * from barang",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            rs.beforeFirst(); 
            while(rs.next()){
                cmbKd_Brg.addItem(rs.getString(1).trim());
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    } 
 
    private void detail_barang(String xkode){
        try{
            stm=Con.createStatement();
            pstat = Con.prepareStatement("select * from barang where kd_brg='"+xkode+"'",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            rs.beforeFirst();
            while(rs.next()){
                txtNm_Brg.setText(rs.getString(2).trim());
                txtHarga.setText(Double.toString((Double)rs.getDouble(4)));
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    }
 
    private void setTombol(boolean t){
        cmdTambah.setEnabled(t);
        cmdSimpan.setEnabled(!t);
        cmdBatal.setEnabled(!t);
        cmdKeluar.setEnabled(t);
        cmdHapusItem.setEnabled(!t);
        cmdCetak.setEnabled(t);
    } 

    private void detail_konsumen(String xkode){
        try{
            stm=Con.createStatement(); 
            pstat = Con.prepareStatement("select * from konsumen where kd_kons='"+xkode+"'",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
 
            rs.beforeFirst();
            while(rs.next()){
                txtNama.setText(rs.getString(2).trim());
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    } 
 
    private void kosong(){
        txtNoJual.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtTotal.setText("");
        text.setText("");
    } 
 
    private void kosong_detail(){
        txtNm_Brg.setText("");
        txtHarga.setText("");
        txtJml.setText("");
        txtTot.setText("");
    } 
 
    private void aktif(boolean x){
        cmbKd_Kons.setEnabled(x);
        cmbKd_Brg.setEnabled(x);
        cmbKd_Kons.removeAllItems();
        cmbKd_Brg.removeAllItems();
        txtTgl.setEnabled(x);
        txtJml.setEditable(x);
    }
 
    private void nomor_jual(){
        try{
            stm=Con.createStatement(); 
            pstat = Con.prepareStatement("SELECT no_jual FROM jual ORDER BY no_jual DESC LIMIT 1",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            int brs=0;
            while(rs.next()){
                brs=rs.getRow();
            }
            if(brs==0)
                txtNoJual.setText("1");
            else{
                rs.beforeFirst();
                while(rs.next()) {
                    int no=rs.getInt("no_jual")+1; 
                    txtNoJual.setText(Integer.toString(no)); 
                }
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    } 

    private void format_tanggal(){
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        int year=c1.get(Calendar.YEAR);
        int month=c1.get(Calendar.MONTH)+1;
        int day=c1.get(Calendar.DAY_OF_MONTH);
        tanggal=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
    } 
 
    private void simpan_ditabel(){
        try{    
            String tKode=cmbKd_Brg.getSelectedItem().toString();
            String tNama=txtNm_Brg.getText();
            double hrg=Double.parseDouble(txtHarga.getText());
            int jml=Integer.parseInt(txtJml.getText());
            double tot=Double.parseDouble(txtTot.getText());
            tableModel.addRow(new Object[]{tKode,tNama,hrg,jml,tot});
            inisialisasi_tabel();
        }catch(Exception e){
            System.out.println("Error : "+e);
        }
    }
    
    private void simpan_transaksi(){
        try{
            String xnojual = txtNoJual.getText();
            format_tanggal();
            String xkode=cmbKd_Kons.getSelectedItem().toString();
            String msql="insert into jual values('"+xnojual+"','"+xkode+"','"+tanggal+"')";
            stm.executeUpdate(msql);
            for(int i=0;i<tblJual.getRowCount();i++){
                String xkd=(String)tblJual.getValueAt(i,0);
                double xhrg=(Double)tblJual.getValueAt(i,2);
                int xjml=(Integer)tblJual.getValueAt(i,3);
                String zsql="insert into djual values('"+xnojual+"','"+xkd+"',"+xhrg+","+xjml+")";
                stm.executeUpdate(zsql);
                //update stok
                String ysql="update barang set stok=stok-"+xjml+" where kd_brg='"+xkd+"'";
                stm.executeUpdate(ysql);
            }
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    } 
 
    private class PrintingTask extends SwingWorker<Object, Object> {
        private final MessageFormat headerFormat;
        private final MessageFormat footerFormat;
        private final boolean interactive;
        private volatile boolean complete = false;
        private volatile String message;
        
        public PrintingTask(MessageFormat header, MessageFormat footer, boolean interactive) {
            this.headerFormat = header;
            this.footerFormat = footer;
            this.interactive = interactive;
        }
 
        @Override
        protected Object doInBackground() {
            try {
                complete = text.print(headerFormat, footerFormat, true, null, null, interactive);
                message = "Printing " + (complete ? "complete" : "canceled");
            } catch (PrinterException ex) {
                message = "Sorry, a printer error occurred";
            } catch (SecurityException ex) {
                message = "Sorry, cannot access the printer due to security reasons";
            }
            return null;
        }
 
        @Override
            protected void done() {
            //message(!complete, message);
            }
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNoJual = new javax.swing.JTextField();
        txtTgl = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        cmbKd_Kons = new javax.swing.JComboBox<>();
        cmbKd_Brg = new javax.swing.JComboBox<>();
        txtNm_Brg = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtJml = new javax.swing.JTextField();
        txtTot = new javax.swing.JTextField();
        cmdHapusItem = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblJual = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        txtTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmdTambah = new javax.swing.JButton();
        cmdSimpan = new javax.swing.JButton();
        cmdBatal = new javax.swing.JButton();
        cmdCetak = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("No Jual");

        jLabel2.setText("Tgl Jual");

        txtTgl.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1686751026921L), null, null, java.util.Calendar.DAY_OF_MONTH));

        jLabel3.setText("Kode Konsumen");

        jLabel4.setText("Nama Konsumen");

        cmbKd_Kons.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        cmbKd_Kons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKd_KonsActionPerformed(evt);
            }
        });

        cmbKd_Brg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", " " }));
        cmbKd_Brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKd_BrgActionPerformed(evt);
            }
        });

        txtJml.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJmlKeyPressed(evt);
            }
        });

        cmdHapusItem.setText("Hapus Item");
        cmdHapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusItemActionPerformed(evt);
            }
        });

        tblJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kd Barang", "Nama Barang", "Harga Barang", "Jumlah", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblJual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblJualMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblJual);

        text.setColumns(20);
        text.setRows(5);
        jScrollPane3.setViewportView(text);

        jLabel5.setText("Total");

        cmdTambah.setText("Tambah");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        cmdSimpan.setText("Simpan");
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });

        cmdBatal.setText("Batal");
        cmdBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBatalActionPerformed(evt);
            }
        });

        cmdCetak.setText("Cetak");
        cmdCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCetakActionPerformed(evt);
            }
        });

        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane3)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNoJual)
                                    .addComponent(txtTgl))
                                .addGap(111, 111, 111)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbKd_Kons, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbKd_Brg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNm_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTot, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmdHapusItem, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(cmdTambah)
                        .addGap(18, 18, 18)
                        .addComponent(cmdSimpan)
                        .addGap(18, 18, 18)
                        .addComponent(cmdBatal)
                        .addGap(18, 18, 18)
                        .addComponent(cmdCetak)
                        .addGap(18, 18, 18)
                        .addComponent(cmdKeluar)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNoJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cmbKd_Kons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKd_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNm_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdHapusItem)
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdTambah)
                    .addComponent(cmdSimpan)
                    .addComponent(cmdBatal)
                    .addComponent(cmdCetak)
                    .addComponent(cmdKeluar))
                .addGap(128, 128, 128))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbKd_KonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKd_KonsActionPerformed
        // TODO add your handling code here:
        JComboBox cKd_Kons = (javax.swing.JComboBox)evt.getSource();
        //Membaca Item Yang Terpilih — > String
        sKd_Kons = (String)cKd_Kons.getSelectedItem();
        detail_konsumen(sKd_Kons);
    }//GEN-LAST:event_cmbKd_KonsActionPerformed

    private void txtJmlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJmlKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            hitung_jual();
            simpan_ditabel();
        }
    }//GEN-LAST:event_txtJmlKeyPressed

    private void cmdHapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusItemActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dataModel = (DefaultTableModel) tblJual.getModel();
        int row=tblJual.getSelectedRow();
 
        if (tblJual.getRowCount() > 0) { 
            dataModel.removeRow(row); 
        }
        double xtot,xhrg;
        int xjml;
        xhrg=Double.parseDouble(txtHarga.getText());
        xjml=Integer.parseInt(txtJml.getText());
        xtot=xhrg*xjml;
        total=total-xtot;
        txtTotal.setText(Double.toString(total));
    }//GEN-LAST:event_cmdHapusItemActionPerformed

    private void tblJualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJualMouseClicked
        // TODO add your handling code here:
        setField();
    }//GEN-LAST:event_tblJualMouseClicked

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        // TODO add your handling code here
        aktif(true);
        setTombol(false);
        kosong();
        baca_konsumen();
        baca_barang();
        nomor_jual();
    }//GEN-LAST:event_cmdTambahActionPerformed

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
        // TODO add your handling code here:
        simpan_transaksi();
        aktif(false);
        setTombol(true);
        kosong();
        kosong_detail();
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        // TODO add your handling code here:
        aktif(false);
        setTombol(true);
        kosong();
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void cmdCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCetakActionPerformed
        // TODO add your handling code here:
        format_tanggal();
        String ctk="Nota Penjualan\nNo : "+txtNoJual.getText()+"\nTanggal : "+tanggal;
        ctk=ctk+"\n"+"-------------------------------------------------------------------------------------------------";
        ctk=ctk+"\n"+"Kode\tNama Barang\tHarga\tJml\tTotal";
        ctk=ctk+"\n"+"-------------------------------------------------------------------------------------------------";
 
        for(int i=0;i<tblJual.getRowCount();i++){
            String xkd=(String)tblJual.getValueAt(i,0);
            String xnama=(String)tblJual.getValueAt(i,1);
            double xhrg=(Double)tblJual.getValueAt(i,2);
            int xjml=(Integer)tblJual.getValueAt(i,3);
            double xtot=(Double)tblJual.getValueAt(i,4);
            ctk=ctk+"\n"+xkd+"\t"+xnama+"\t"+xhrg+"\t"+xjml+"\t"+xtot;
        }
        ctk=ctk+"\n"+"-------------------------------------------------------------------------------------------------";
        text.setText(ctk);
        String headerField="";
        String footerField="";
        MessageFormat header = new MessageFormat(headerField);
        MessageFormat footer = new MessageFormat(footerField);;
        boolean interactive = true;//interactiveCheck.isSelected();
        boolean background = true;//backgroundCheck.isSelected();
        PrintingTask task = new PrintingTask(header, footer, interactive);
        if (background) {
            task.execute();
        } else {
            task.run();
        }
    }//GEN-LAST:event_cmdCetakActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        // TODO add your handling code here:
       
        new frmMenu().setVisible(true);
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void cmbKd_BrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKd_BrgActionPerformed
        // TODO add your handling code here:
        JComboBox cKd_Brg = (javax.swing.JComboBox)evt.getSource();
        //Membaca Item Yang Terpilih — > String
        sKd_Brg = (String)cKd_Brg.getSelectedItem();
        detail_barang(sKd_Brg);
        txtJml.setText("");
        txtTot.setText("");
    }//GEN-LAST:event_cmbKd_BrgActionPerformed

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
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbKd_Brg;
    private javax.swing.JComboBox<String> cmbKd_Kons;
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdCetak;
    private javax.swing.JButton cmdHapusItem;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdSimpan;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tblJual;
    private javax.swing.JTextArea text;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJml;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNm_Brg;
    private javax.swing.JTextField txtNoJual;
    private javax.swing.JSpinner txtTgl;
    private javax.swing.JTextField txtTot;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
