package com.mabawa.nnpdairy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabawa.nnpdairy.models.User_Rolez;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class JsonType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.LONGVARCHAR};
    }

    @Override
    public Class returnedClass() {
        return User_Rolez.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null) {
            return y == null;
        } else {
            return x.equals(y);
        }
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(cellContent.getBytes("UTF-8"), this.returnedClass());
            } catch (Exception var7) {
                throw new RuntimeException("Failed to convert String to Invoice: " + var7.getMessage(), var7);
            }
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, Object value, int idx, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            ps.setNull(idx, 1111);
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                StringWriter w = new StringWriter();
                mapper.writeValue(w, value);
                w.flush();
                ps.setObject(idx, w.toString(), 1111);
            } catch (Exception var7) {
                throw new RuntimeException("Failed to convert Invoice to String: " + var7.getMessage(), var7);
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            bos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            return (new ObjectInputStream(bais)).readObject();
        } catch (IOException | ClassNotFoundException var5) {
            throw new HibernateException(var5);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)this.deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
