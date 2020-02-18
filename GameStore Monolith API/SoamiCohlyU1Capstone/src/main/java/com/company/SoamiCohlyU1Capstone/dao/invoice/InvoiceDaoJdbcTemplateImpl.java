package com.company.SoamiCohlyU1Capstone.dao.invoice;


import com.company.SoamiCohlyU1Capstone.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceDaoJdbcTemplateImpl implements InvoiceDao {

    private JdbcTemplate jdbcTemplate;

    //Prepared statements
    private static final String INSERT_INVOICE_SQL =
            "INSERT INTO invoice (name, street, city, state, zipcode, item_type,item_id, unit_price, quantity," +
                    "subtotal, tax, processing_fee, total) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SELECT_INVOICE_SQL =
            "SELECT * FROM invoice where invoice_id = ?";

    private static final String SELECT_ALL_INVOICES_SQL =
            "SELECT* FROM invoice";

    private static final String UPDATE_INVOICE_SQL =
            "UPDATE invoice set name = ?, street = ?, city = ?, state = ?, zipcode = ?, item_type = ?,item_id = ?, unit_price = ?," +
                    " quantity = ?, subtotal = ?, tax = ?, processing_fee = ?, total = ? where invoice_id = ? ";

    private static final String DELETE_INVOICE_SQL =
            "DELETE FROM invoice WHERE invoice_id = ?";

    @Autowired
    public InvoiceDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    @Override
    public Invoice addInvoice(Invoice invoice) {
        jdbcTemplate.update(
                INSERT_INVOICE_SQL,
                invoice.getName(),
                invoice.getStreet(),
                invoice.getCity(),
                invoice.getState(),
                invoice.getZipCode(),
                invoice.getItemType(),
                invoice.getItemId(),
                invoice.getUnitPrice(),
                invoice.getQuantity(),
                invoice.getSubTotal(),
                invoice.getTax(),
                invoice.getProcessingFee(),
                invoice.getTotal());

        int id = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);
        invoice.setInvoiceId(id);
        return invoice;
    }

    @Override
    public Invoice getInvoice(int id) {

        try {
            return jdbcTemplate.queryForObject(SELECT_INVOICE_SQL, this::mapRowToInvoice, id);
        } catch (EmptyResultDataAccessException e) {
            // if there is no match for this album id, return null
            return null;
        }
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return jdbcTemplate.query(SELECT_ALL_INVOICES_SQL,this::mapRowToInvoice);
    }


    @Override
    public void updateInvoice(Invoice invoice) {
        jdbcTemplate.update(
                UPDATE_INVOICE_SQL,
                invoice.getName(),
                invoice.getStreet(),
                invoice.getCity(),
                invoice.getState(),
                invoice.getZipCode(),
                invoice.getItemType(),
                invoice.getItemId(),
                invoice.getUnitPrice(),
                invoice.getQuantity(),
                invoice.getSubTotal(),
                invoice.getTax(),
                invoice.getProcessingFee(),
                invoice.getTotal(),
                invoice.getInvoiceId());
    }

    @Override
    public void deleteInvoice(int id) {jdbcTemplate.update(DELETE_INVOICE_SQL,id); }

    //RowMapper
    private Invoice mapRowToInvoice(ResultSet rs, int rowNum) throws SQLException{
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setName(rs.getString("name"));
        invoice.setStreet(rs.getString("street"));
        invoice.setCity(rs.getString("city"));
        invoice.setState(rs.getString("state"));
        invoice.setZipCode(rs.getString("zipcode"));
        invoice.setItemType(rs.getString("item_type"));
        invoice.setItemId(rs.getInt("item_id"));
        invoice.setUnitPrice(rs.getBigDecimal("unit_price"));
        invoice.setQuantity(rs.getInt("quantity"));
        invoice.setSubTotal(rs.getBigDecimal("subtotal"));
        invoice.setTax(rs.getBigDecimal("tax"));
        invoice.setProcessingFee(rs.getBigDecimal("processing_fee"));
        invoice.setTotal(rs.getBigDecimal("total"));

        return invoice;
    }
}
