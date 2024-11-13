/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package penjualan;

/**
 *
 * @author Bejo555
 */
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.*;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;


public class frmBeli extends javax.swing.JFrame {

    /**
     * Creates new form frmTransaksi
     */
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
    
     DefaultTableModel tableModel = new DefaultTableModel(new Object [][]{}, new String [] {"Kd Barang", "NamaBarang","Harga Barang","Jumlah","Total"});
    
    public frmBeli() {
    initComponents();
    open_db();
    inisialisasi_tabel();
    aktif(false);
    setTombol(true);
    cmdCetak.setEnabled(false);
    txtTgl.setEditor(new JSpinner.DateEditor(txtTgl, "yyyy/MM/dd"));
    }
    
    private void setField(){
        int row = tblBeli.getSelectedRow();
        cmbKd_Brg.setSelectedItem((String)tblBeli.getValueAt(row,0));
        txtNm_Brg.setText((String)tblBeli.getValueAt(row,1));
        String harga = Double.toString((Double)tblBeli.getValueAt(row,2));
        txtHarga.setText(harga);
        String jumlah = Integer.toString((Integer)tblBeli.getValueAt(row,3));
        txtJml.setText(jumlah);
        String total = Double.toString((Double)tblBeli.getValueAt(row, 4));
        txtTot.setText(total);
        
    }
    private void hitung_jual(){
        double xtot,xhrg;
        int xjml;
        xhrg=Double.parseDouble(txtHarga.getText());
        xjml=Integer.parseInt(txtJml.getText());
        xtot = xhrg*xjml;
        String xtotal = Double.toString(xtot);
        txtTot.setText(xtotal);
        total = total+xtot;
        txtTotal.setText(Double.toString(total));
    }
    private void open_db(){
    try{
        KoneksiMysql kon = new KoneksiMysql ("localhost","root","","penjualan");
        Con = kon.getConnection();
 //System.out.println("Berhasil ");
    }catch (Exception e) {
        System.out.println("Error open db : "+e);
    }
    }
    
    private void baca_konsumen(){
        try{
        stm = Con.createStatement();
        pstat = Con.prepareStatement("select kd_pmk,nm_pmk from pemasok",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = pstat.executeQuery();
        
        rs.beforeFirst();
        while(rs.next()){
        cmbKd_Kons.addItem(rs.getString(1).trim());
        }
        rs.close();
        }catch(Exception e){
        System.out.println("Error: "+e);
        }
    }
    
    public void inisialisasi_tabel()
    {
        tblBeli.setModel(tableModel);
    }
    private void baca_barang(){
        try{
            stm = Con.createStatement();
            pstat = Con.prepareStatement("select * from barang",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            rs.beforeFirst();
            while(rs.next())
            {
                cmbKd_Brg.addItem(rs.getString(1).trim());
            }
            rs.close();

        }catch(SQLException e){
            System.out.println("ERROR: "+e);
        }
    }
    private void detail_barang(String xkode){
        try{
            stm=Con.createStatement();
            
            pstat = Con.prepareStatement("select * from barang where kd_brg="+xkode+"",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            rs.beforeFirst();
            while(rs.next())
            {
                txtNm_Brg.setText(rs.getString(2).trim());
                txtHarga.setText(Double.toString((Double)rs.getDouble(5)));
            }
            rs.close();

        }catch(SQLException e){
            System.out.println("ERROR"+e);
        }
    }
    
    private void setTombol(boolean t){
        cmdTambah.setEnabled(t);
        cmdSimpan.setEnabled(!t);
        cmdBatal.setEnabled(!t);
        cmdKeluar.setEnabled(t);
        cmdHapusItem.setEnabled(!t);
    }
    
    private void detail_konsumen(String xkode){
        try{
            stm=Con.createStatement();
            pstat = Con.prepareStatement("select * from pemasok where kd_pmk='"+xkode+"'",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstat.executeQuery();
            rs.beforeFirst();
            while(rs.next()){
                txtNama.setText(rs.getString(2).trim());
            }
            rs.close();
            
        }catch(SQLException e){
            System.out.println("ERROR: "+e);
        }
    }
    private void kosong(){
        txtNoJual.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtTotal.setText("");
        text.setText("");
    }
    
    private void kosong_detail()
    {
        txtNm_Brg.setText("");
        txtHarga.setText("");
        txtJml.setText("");
        txtTot.setText("");
    }
    
    private void aktif(boolean x)
    {
        cmbKd_Kons.setEnabled(x);
        cmbKd_Brg.setEnabled(x);
        cmbKd_Kons.removeAllItems();
        cmbKd_Brg.removeAllItems();
        txtTgl.setEnabled(x);
        txtJml.setEditable(x);
    }
    
    private void nomor_jual()
    {
    try{
        stm=Con.createStatement(); 
        pstat = Con.prepareStatement("SELECT no_beli FROM beli ORDER BY no_beli DESC LIMIT 1",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
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
        int no=rs.getInt("no_beli")+1; 
        txtNoJual.setText(Integer.toString(no)); 
       }
       }
       rs.close();
       }catch(SQLException e){
        System.out.println("Error : "+e);
       }
    } 
    
    private void format_tanggal()
    {
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
            String tKode = cmbKd_Brg.getSelectedItem().toString();
            String tNama = txtNm_Brg.getText();
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
            xnojual=txtNoJual.getText();
            format_tanggal();
            String xkode=cmbKd_Kons.getSelectedItem().toString();
            String msql="insert into beli values('"+xnojual+"','"+xkode+"','"+tanggal+"')";
            stm.executeUpdate(msql);
            for(int i=0;i<tblBeli.getRowCount();i++)
            {
                String xkd=(String)tblBeli.getValueAt(i,0); 
                double xhrg=(Double)tblBeli.getValueAt(i,2); 
                int xjml=(Integer)tblBeli.getValueAt(i,3);
                String zsql="insert into dbeli values('"+xnojual+"','"+xkd+"',"+xhrg+","+xjml+")";  stm.executeUpdate(zsql); 
                //update stok 
                String ysql="update barang set stok=stok+"+xjml+" where kd_brg='"+xkd+"'";  stm.executeUpdate(ysql); 

            }
        }catch(SQLException e){
            System.out.println("error"+e);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNoJual = new javax.swing.JTextField();
        txtTgl = new javax.swing.JSpinner();
        cmbKd_Kons = new javax.swing.JComboBox<>();
        txtNama = new javax.swing.JTextField();
        cmbKd_Brg = new javax.swing.JComboBox<>();
        txtNm_Brg = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtJml = new javax.swing.JTextField();
        txtTot = new javax.swing.JTextField();
        cmdHapusItem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBeli = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        cmdTambah = new javax.swing.JButton();
        cmdSimpan = new javax.swing.JButton();
        cmdBatal = new javax.swing.JButton();
        cmdCetak = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("No Beli");

        jLabel2.setText("Tgl Beli");

        jLabel3.setText("Kode Pemasok");

        jLabel4.setText("Nama Pemasok");

        txtTgl.setModel(new javax.swing.SpinnerDateModel());

        cmbKd_Kons.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKd_Kons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKd_KonsActionPerformed(evt);
            }
        });

        cmbKd_Brg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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

        cmdHapusItem.setText("Hapus");
        cmdHapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusItemActionPerformed(evt);
            }
        });

        tblBeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "kdBarang", "NamaBarang", "Harga Barang", "Title 4", "Total"
            }
        ));
        tblBeli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBeliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBeli);

        text.setColumns(20);
        text.setRows(5);
        jScrollPane2.setViewportView(text);

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

        jLabel7.setText("Harga Barang");

        jLabel8.setText("Kode Barang");

        jLabel9.setText("Nama Barang");

        jLabel10.setText("Jumlah Barang");

        jLabel11.setText("Total Harga");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(68, 68, 68)
                                .addComponent(jLabel9)
                                .addGap(68, 68, 68)
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbKd_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNm_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtTot, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                                        .addComponent(cmdHapusItem))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmdTambah)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdSimpan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdBatal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdCetak)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdKeluar)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNoJual)
                            .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbKd_Kons, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdHapusItem)
                    .addComponent(cmbKd_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNm_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdSimpan)
                    .addComponent(cmdBatal)
                    .addComponent(cmdCetak)
                    .addComponent(cmdTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdKeluar))
                .addGap(141, 141, 141))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbKd_KonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKd_KonsActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
    JComboBox cKd_Kons = (javax.swing.JComboBox)evt.getSource();
    //Membaca Item Yang Terpilih — > String
     sKd_Kons = (String)cKd_Kons.getSelectedItem();
     detail_konsumen(sKd_Kons);

    }//GEN-LAST:event_cmbKd_KonsActionPerformed

    private void cmbKd_BrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKd_BrgActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JComboBox cKd_Brg = (javax.swing.JComboBox)evt.getSource();
        //Membaca Item Yang Terpilih — > String
        sKd_Brg = (String)cKd_Brg.getSelectedItem();
        detail_barang(sKd_Brg);
        txtJml.setText("");
        txtTot.setText(""); 
    }//GEN-LAST:event_cmbKd_BrgActionPerformed

    private void txtJmlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJmlKeyPressed
        // TODO add your handling code here:
        // TODO add your handling code here: 
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        hitung_jual();
        simpan_ditabel();
        }
    }//GEN-LAST:event_txtJmlKeyPressed

    private void cmdHapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusItemActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here: 
        DefaultTableModel dataModel = (DefaultTableModel) tblBeli.getModel();
        int row=tblBeli.getSelectedRow();

        if (tblBeli.getRowCount() > 0) { 
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

    private void tblBeliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBeliMouseClicked
        // TODO add your handling code here:
        // TODO add your handling code here:
        setField();
    }//GEN-LAST:event_tblBeliMouseClicked

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here: 
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
        cmdCetak.setEnabled(true);
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        // TODO add your handling code here:
        aktif(false);
        setTombol(true);
        kosong();
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void cmdCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCetakActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        format_tanggal();
        String ctk="Nota Penjualan\nNo : "+xnojual+"\nTanggal : "+tanggal;
        ctk=ctk+"\n"+"-------------------------------------------------------------------------------------------------";
        ctk=ctk+"\n"+"Kode\tNama Barang\tHarga\tJml\tTotal";
        ctk=ctk+"\n"+"-------------------------------------------------------------------------------------------------";
        for(int i=0;i<tblBeli.getRowCount();i++)
        {
            String xkd=(String)tblBeli.getValueAt(i,0);
            String xnama=(String)tblBeli.getValueAt(i,1);
            double xhrg=(Double)tblBeli.getValueAt(i,2);
            int xjml=(Integer)tblBeli.getValueAt(i,3);
            double xtot=(Double)tblBeli.getValueAt(i,4);
            ctk=ctk+"\n"+xkd+"\t"+xnama+"\t"+xhrg+"\t"+xjml+"\t"+xtot;
        }
        ctk=ctk+"\n"+"-------------------------------------------------------------------------------------------------";
        ctk=ctk+"\n"+"\t\t\tTotal Harga   :"+total;
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
            java.util.logging.Logger.getLogger(frmBeli.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmBeli.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmBeli.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmBeli.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmBeli().setVisible(true);
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblBeli;
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
