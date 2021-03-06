package vistas.listaClientes;

import Informes.InformeClientes;
import com.itextpdf.text.DocumentException;
import controlador.Controlador;
import entidades.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class VistaListaClientes extends JPanel{
    private JLabel textoCliente;
    private JButton botonAgregar;
    private JButton botonEliminar;
    private JButton botonEditar;
    private JTable tablaClientes;
    private JPanel panelListaClientes;
    private JScrollPane scrollTablaClientes;
    private JButton botonImprimirInforme;

    private static int idCleinteSeleccionado;
    private static String nombreCleinteSeleccionado;

    DefaultTableModel model = new DefaultTableModel();

    VistaListaClientes(){
        setHeaders();
        fillTable();
        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = String.valueOf(JOptionPane.showInputDialog("Introduce nombre"));
                Controlador.agregarCliente(nombre);
                fillTable();

            }
        });
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int filaPunto = tablaClientes.rowAtPoint(e.getPoint()); //fila seleccionada
                int columnaPunto = 0;
                if (filaPunto > -1){
                    idCleinteSeleccionado = Integer.parseInt(String.valueOf(model.getValueAt(filaPunto, columnaPunto)));
                    nombreCleinteSeleccionado = (String.valueOf(model.getValueAt(filaPunto, 1)));

                }
            }
        });
        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controlador.removeCliente(idCleinteSeleccionado);
                fillTable();
            }
        });
        botonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoNombre = JOptionPane.showInputDialog("Introduce el nuevo nombre para " + nombreCleinteSeleccionado);
                Controlador.updateCliente(idCleinteSeleccionado, nuevoNombre);
                fillTable();
            }
        });
        botonImprimirInforme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InformeClientes informeClientes = new InformeClientes();
                try {
                    informeClientes.generarInforme(Controlador.getListaClientes());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (DocumentException documentException) {
                    documentException.printStackTrace();
                }
            }
        });
    }

    private void setHeaders(){
        this.tablaClientes = new JTable(model);
        scrollTablaClientes.setViewportView(tablaClientes);
        model.addColumn("Identificador");
        model.addColumn("Nombre");

    }

    private void fillTable(){
        ArrayList<Cliente> listaClientes = Controlador.getListaClientes();
        model.setRowCount(0); //Limpiar la tabla
        for(Cliente cliente : listaClientes){
            Object[] datosNuevoCliente = new Object[2];
            datosNuevoCliente[0] = cliente.getIdCliente();
            datosNuevoCliente[1] = cliente.getNombre();
            model.addRow(datosNuevoCliente);
        }
    }



    public JPanel getPanelListaClientes(){
        return this.panelListaClientes;
    }
}

