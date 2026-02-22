package cibertec.pe.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import cibertec.pe.config.SoapConsumerConfig;
import cibertec.pe.wsgestionpedido.*;

public class SistemaGestionPedidos extends JFrame {

    private static final long serialVersionUID = 1L;

    private GestionPedidoImplement service = SoapConsumerConfig.getService();

    private DefaultTableModel modelClientes;
    private DefaultTableModel modelProductos;
    private DefaultTableModel modelRepartidores;
    private DefaultTableModel modelListadoPedidos;

    public SistemaGestionPedidos() {
        setTitle("Sistema de Gestión de Pedidos - Proyecto DSWII");
        setSize(1050, 750);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("👥 Clientes", panelClientes());
        tabs.addTab("📦 Productos", panelProductos());
        tabs.addTab("🛵 Repartidores", panelRepartidores());
        tabs.addTab("🛒 Nuevo Pedido", panelNuevoPedido());
        tabs.addTab("⚙️ Gestión Pedidos", panelGestionPedidos());
        tabs.addTab("📋 Listado Pedidos", panelListadoPedidos());

        getContentPane().add(tabs);

        cargarDatosClientes();
        cargarDatosProductos();
        cargarDatosRepartidores();
        cargarDatosPedidos();
    }

    private JPanel panelClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Mantenimiento de Clientes"));

        JTextField txtId = new JTextField();
        txtId.setToolTipText("Requerido para actualizar o eliminar");
        JTextField txtNombre = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();

        form.add(new JLabel("ID Cliente (Solo Act/Elim):"));
        form.add(txtId);
        form.add(new JLabel("Nombre Completo:"));
        form.add(txtNombre);
        form.add(new JLabel("Teléfono (Único):"));
        form.add(txtTelefono);
        form.add(new JLabel("Dirección:"));
        form.add(txtDireccion);

        JPanel panelBotones = new JPanel();
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        modelClientes = new DefaultTableModel(new String[] { "ID", "Nombre", "Teléfono", "Dirección" }, 0);
        JTable tablaClientes = new JTable(modelClientes);

        btnCrear.addActionListener(e -> {
            if (camposVacios(txtNombre, txtTelefono, txtDireccion))
                return;
            try {
                Cliente c = new Cliente();
                c.setNombre(txtNombre.getText().trim());
                c.setTelefono(txtTelefono.getText().trim());
                c.setDireccion(txtDireccion.getText().trim());

                service.crearCliente(c);
                JOptionPane.showMessageDialog(this, "Cliente creado exitosamente.");
                cargarDatosClientes();
                limpiarCampos(txtId, txtNombre, txtTelefono, txtDireccion);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActualizar.addActionListener(e -> {
            if (camposVacios(txtId, txtNombre, txtTelefono, txtDireccion))
                return;
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                Cliente c = new Cliente();
                c.setNombre(txtNombre.getText().trim());
                c.setTelefono(txtTelefono.getText().trim());
                c.setDireccion(txtDireccion.getText().trim());

                service.actualizarCliente(id, c);
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
                cargarDatosClientes();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error de Formato",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar. " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            if (camposVacios(txtId))
                return;
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                String msg = service.eliminarCliente(id);
                JOptionPane.showMessageDialog(this, msg);
                cargarDatosClientes();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error de Formato",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Mantenimiento de Productos"));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtPrecio = new JTextField();

        form.add(new JLabel("ID Producto (Solo Act/Elim):"));
        form.add(txtId);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Descripción:"));
        form.add(txtDesc);
        form.add(new JLabel("Precio (S/):"));
        form.add(txtPrecio);

        JPanel panelBotones = new JPanel();
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        modelProductos = new DefaultTableModel(new String[] { "ID", "Nombre", "Descripción", "Precio" }, 0);
        JTable tablaProductos = new JTable(modelProductos);

        btnCrear.addActionListener(e -> {
            if (camposVacios(txtNombre, txtDesc, txtPrecio))
                return;
            try {
                Producto p = new Producto();
                p.setNombre(txtNombre.getText().trim());
                p.setDescripcion(txtDesc.getText().trim());
                p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));

                service.crearProducto(p);
                JOptionPane.showMessageDialog(this, "Producto registrado.");
                cargarDatosProductos();
                limpiarCampos(txtId, txtNombre, txtDesc, txtPrecio);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El precio debe ser numérico (ej: 15.50).", "Error",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActualizar.addActionListener(e -> {
            if (camposVacios(txtId, txtNombre, txtDesc, txtPrecio))
                return;
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                Producto p = new Producto();
                p.setNombre(txtNombre.getText().trim());
                p.setDescripcion(txtDesc.getText().trim());
                p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));

                String res = service.actualizarProducto(id, p);
                JOptionPane.showMessageDialog(this, res);
                cargarDatosProductos();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID o Precio tienen formato incorrecto.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            if (camposVacios(txtId))
                return;
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                String res = service.eliminarProducto(id);
                JOptionPane.showMessageDialog(this, res);
                cargarDatosProductos();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelRepartidores() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Registrar Repartidor"));

        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Apellido:"));
        form.add(txtApellido);

        JButton btnCrear = new JButton("Registrar Repartidor");

        modelRepartidores = new DefaultTableModel(new String[] { "ID", "Nombre", "Apellido" }, 0);
        JTable tablaRepartidores = new JTable(modelRepartidores);

        btnCrear.addActionListener(e -> {
            if (camposVacios(txtNombre, txtApellido))
                return;
            try {
                Repartidor r = new Repartidor();
                r.setNombre(txtNombre.getText().trim());
                r.setApellido(txtApellido.getText().trim());
                service.crearRepartidor(r);
                JOptionPane.showMessageDialog(this, "Repartidor registrado con éxito.");
                cargarDatosRepartidores();
                limpiarCampos(txtNombre, txtApellido);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(form, BorderLayout.CENTER);
        panelTop.add(btnCrear, BorderLayout.SOUTH);

        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaRepartidores), BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelNuevoPedido() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel cabecera = new JPanel(new GridLayout(2, 2, 5, 5));
        cabecera.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));
        JTextField txtTelCliente = new JTextField();
        JTextField txtIdRepartidor = new JTextField();
        cabecera.add(new JLabel("Teléfono del Cliente:"));
        cabecera.add(txtTelCliente);
        cabecera.add(new JLabel("ID Repartidor:"));
        cabecera.add(txtIdRepartidor);

        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del Pedido"));

        JPanel formDetalle = new JPanel(new FlowLayout());
        JTextField txtIdProducto = new JTextField(5);
        JSpinner spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAddDetalle = new JButton("Agregar Producto");

        formDetalle.add(new JLabel("ID Producto:"));
        formDetalle.add(txtIdProducto);
        formDetalle.add(new JLabel("Cantidad:"));
        formDetalle.add(spCantidad);
        formDetalle.add(btnAddDetalle);

        DefaultTableModel modelDetalle = new DefaultTableModel(new String[] { "ID Producto", "Cantidad" }, 0);
        JTable tablaDetalles = new JTable(modelDetalle);

        btnAddDetalle.addActionListener(e -> {
            if (camposVacios(txtIdProducto))
                return;
            try {
                Long.parseLong(txtIdProducto.getText().trim());
                modelDetalle.addRow(new Object[] { txtIdProducto.getText().trim(), spCantidad.getValue() });
                txtIdProducto.setText("");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID del producto debe ser un número válido.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        panelDetalles.add(formDetalle, BorderLayout.NORTH);
        panelDetalles.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        JPanel panelBotonCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonCentral.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton btnRegistrarPedido = new JButton("ENVIAR PEDIDO A SERVIDOR (SOAP)");
        btnRegistrarPedido.setPreferredSize(new Dimension(350, 45));
        btnRegistrarPedido.setBackground(new Color(40, 167, 69));
        btnRegistrarPedido.setForeground(Color.WHITE);
        btnRegistrarPedido.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegistrarPedido.setFocusPainted(false);
        btnRegistrarPedido.setOpaque(true);
        btnRegistrarPedido.setContentAreaFilled(true);

        panelBotonCentral.add(btnRegistrarPedido);

        btnRegistrarPedido.addActionListener(e -> {
            if (camposVacios(txtTelCliente, txtIdRepartidor))
                return;
            try {
                PedidoRequest req = new PedidoRequest();
                req.setTelefonoCliente(txtTelCliente.getText().trim());
                req.setIdRepartidor(Long.parseLong(txtIdRepartidor.getText().trim()));

                int filas = modelDetalle.getRowCount();
                if (filas == 0)
                    throw new Exception("Debe agregar al menos un producto a la lista.");

                for (int i = 0; i < filas; i++) {
                    DetallePedidoRequest det = new DetallePedidoRequest();
                    det.setIdProducto(Long.parseLong(modelDetalle.getValueAt(i, 0).toString()));
                    det.setCantidad(Integer.parseInt(modelDetalle.getValueAt(i, 1).toString()));
                    req.getDetalles().add(det);
                }

                PedidoResponse res = service.registrarPedido(req);
                JOptionPane.showMessageDialog(this, "Pedido procesado exitosamente.\nTotal: S/ " + res.getTotal());
                modelDetalle.setRowCount(0);
                limpiarCampos(txtTelCliente, txtIdRepartidor);
                cargarDatosPedidos();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID del Repartidor debe ser un número.", "Error de Formato",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al procesar pedido: " + ex.getMessage(), "Fallo en Pedido",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(cabecera, BorderLayout.NORTH);
        panel.add(panelDetalles, BorderLayout.CENTER);
        panel.add(panelBotonCentral, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelGestionPedidos() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlEstado = new JPanel(new FlowLayout());
        pnlEstado.setBorder(BorderFactory.createTitledBorder("Cambiar Estado de Pedido"));
        JTextField txtIdPedidoEstado = new JTextField(5);
        JComboBox<EstadoPedido> cbEstado = new JComboBox<>(EstadoPedido.values());
        JButton btnCambiarEstado = new JButton("Actualizar Estado");

        pnlEstado.add(new JLabel("ID Pedido:"));
        pnlEstado.add(txtIdPedidoEstado);
        pnlEstado.add(new JLabel("Nuevo Estado:"));
        pnlEstado.add(cbEstado);
        pnlEstado.add(btnCambiarEstado);

        JPanel pnlPago = new JPanel(new FlowLayout());
        pnlPago.setBorder(BorderFactory.createTitledBorder("Registrar Pago"));
        JTextField txtIdPedidoPago = new JTextField(5);
        JComboBox<MetodoPago> cbPago = new JComboBox<>(MetodoPago.values());
        JButton btnRegistrarPago = new JButton("Registrar Pago");

        pnlPago.add(new JLabel("ID Pedido:"));
        pnlPago.add(txtIdPedidoPago);
        pnlPago.add(new JLabel("Método de Pago:"));
        pnlPago.add(cbPago);
        pnlPago.add(btnRegistrarPago);

        btnCambiarEstado.addActionListener(e -> {
            if (camposVacios(txtIdPedidoEstado))
                return;
            try {
                Long id = Long.parseLong(txtIdPedidoEstado.getText().trim());
                EstadoPedido estado = (EstadoPedido) cbEstado.getSelectedItem();
                service.cambiarEstado(id, estado);
                JOptionPane.showMessageDialog(this, "Estado actualizado a " + estado);
                cargarDatosPedidos();
                limpiarCampos(txtIdPedidoEstado);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID del pedido debe ser un número.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegistrarPago.addActionListener(e -> {
            if (camposVacios(txtIdPedidoPago))
                return;
            try {
                Long id = Long.parseLong(txtIdPedidoPago.getText().trim());
                MetodoPago pago = (MetodoPago) cbPago.getSelectedItem();
                service.registrarPagoFinal(id, pago);
                JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.");
                cargarDatosPedidos();
                limpiarCampos(txtIdPedidoPago);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID del pedido debe ser un número.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(pnlEstado);
        panel.add(pnlPago);
        return panel;
    }

    private JPanel panelListadoPedidos() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton btnRefrescar = new JButton("🔄 Refrescar Lista");
        btnRefrescar.addActionListener(e -> cargarDatosPedidos());

        modelListadoPedidos = new DefaultTableModel(new String[] {
                "Cliente", "Fecha", "Estado", "Total (S/)", "Método Pago", "Repartidor"
        }, 0);
        JTable tablaPedidos = new JTable(modelListadoPedidos);

        panel.add(btnRefrescar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        return panel;
    }

    private boolean camposVacios(JTextField... campos) {
        for (JTextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos requeridos.", "Campos Vacíos",
                        JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }

    private void cargarDatosClientes() {
        try {
            modelClientes.setRowCount(0);
            List<Cliente> lista = service.listarClientes();
            if (lista != null) {
                for (Cliente c : lista) {
                    modelClientes.addRow(new Object[] { c.getId(), c.getNombre(), c.getTelefono(), c.getDireccion() });
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void cargarDatosProductos() {
        try {
            modelProductos.setRowCount(0);
            List<Producto> lista = service.listarProductos();
            if (lista != null) {
                for (Producto p : lista) {
                    modelProductos.addRow(new Object[] { p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio() });
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void cargarDatosRepartidores() {
        try {
            modelRepartidores.setRowCount(0);
            List<Repartidor> lista = service.listarRepartidores();
            if (lista != null) {
                for (Repartidor r : lista) {
                    modelRepartidores.addRow(new Object[] { r.getId(), r.getNombre(), r.getApellido() });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar repartidores.\nDetalle técnico: " + ex.getMessage() +
                            "\n\nSugerencia: Verifica en tu servidor Spring Boot si olvidaste poner @XmlTransient en la entidad Repartidor.",
                    "Fallo de Conexión SOAP", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosPedidos() {
        try {
            if (modelListadoPedidos != null) {
                modelListadoPedidos.setRowCount(0);
                List<PedidoResponse> lista = service.listarPedidos();
                if (lista != null) {
                    for (PedidoResponse pr : lista) {
                        modelListadoPedidos.addRow(new Object[] {
                                pr.getCliente(), pr.getFecha(), pr.getEstado(), pr.getTotal(), pr.getMetodoPago(),
                                pr.getRepartidor()
                        });
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        SwingUtilities.invokeLater(() -> new SistemaGestionPedidos().setVisible(true));
    }
}