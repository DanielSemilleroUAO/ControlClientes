/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import datos.ClienteDao;
import dominio.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author adseglocdom
 */
@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "editar":
                    editarCliente(req, resp);
                    break;
                case "eliminar":
                    eliminarCliente(req, resp);
                    break;
                default:
                    accionDefault(req, resp);
                    break;
            }
        } else {
            accionDefault(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    insertarCliente(req, resp);
                    break;
                case "modificar":
                    modificarCliente(req, resp);
                    break;
                default:
                    accionDefault(req, resp);
                    break;
            }
        } else {
            accionDefault(req, resp);
        }
    }

    private double calcularSaldoTotal(List<Cliente> clientes) {
        double saldoTotal = 0;
        for (Cliente cliente : clientes) {
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }

    private void accionDefault(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Cliente> clientes = new ClienteDao().listar();
        HttpSession sesion = req.getSession();
        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("totalClientes", clientes.size());
        sesion.setAttribute("saldoTotal", calcularSaldoTotal(clientes));
        // req.getRequestDispatcher("clientes.jsp").forward(req, resp);
        resp.sendRedirect("clientes.jsp");
    }

    private void insertarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        double saldo = 0;
        String saldoFormulario = req.getParameter("saldo");
        saldo = (saldoFormulario != null && !saldoFormulario.equals("")) ? Double.parseDouble(saldoFormulario) : 0;

        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);

        int registroModificados = new ClienteDao().insertar(cliente);

        accionDefault(req, resp);
    }

    private void editarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        Cliente cliente = new ClienteDao().encontrarCliente(new Cliente(idCliente));

        req.setAttribute("cliente", cliente);

        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        req.getRequestDispatcher(jspEditar).forward(req, resp);

    }

    private void modificarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        double saldo = 0;
        String saldoFormulario = req.getParameter("saldo");
        saldo = (saldoFormulario != null && !saldoFormulario.equals("")) ? Double.parseDouble(saldoFormulario) : 0;

        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);

        int registroModificados = new ClienteDao().actualizar(cliente);

        accionDefault(req, resp);

    }
    
    private void eliminarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int idCliente = Integer.parseInt(req.getParameter("idCliente"));

        Cliente cliente = new Cliente(idCliente);

        int registroModificados = new ClienteDao().eliminar(cliente);

        accionDefault(req, resp);

    }

}
