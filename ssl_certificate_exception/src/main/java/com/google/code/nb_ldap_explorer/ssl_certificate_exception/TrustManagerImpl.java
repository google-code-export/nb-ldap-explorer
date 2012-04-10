package com.google.code.nb_ldap_explorer.ssl_certificate_exception;

import com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui.CertificateCheckDialog;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

public class TrustManagerImpl implements X509TrustManager {

    private X509TrustManager parentTrustmanager;
    private KeyStore userkeystore;

    public TrustManagerImpl(X509TrustManager parentTrustmanager, KeyStore userkeystore) {
        this.parentTrustmanager = parentTrustmanager;
        this.userkeystore = userkeystore;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        if (parentTrustmanager != null) {
            return parentTrustmanager.getAcceptedIssuers();
        } else {
            return null;
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs,
            String authType) throws CertificateException {
        parentTrustmanager.checkClientTrusted(certs, authType);
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] certs,
            final String authType) throws CertificateException {
        try {
            parentTrustmanager.checkServerTrusted(certs, authType);
        } catch (final CertificateException ex) {
            if (isLastCertificateTrusted(certs)) {
                return;
            }
            final AtomicInteger resultRef = new AtomicInteger();
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        CertificateCheckDialog ccd = new CertificateCheckDialog(certs, ex);
                        ccd.setVisible(true);
                        resultRef.set(ccd.getResult());
                    }
                });
            } catch (InterruptedException ex1) {
                Exceptions.printStackTrace(ex1);
            } catch (InvocationTargetException ex1) {
                Exceptions.printStackTrace(ex1);
            }
            int result = resultRef.get();
            if (result != CertificateCheckDialog.ACCEPT
                    && result != CertificateCheckDialog.PERMANENT) {
                throw ex;
            }
            if (result == CertificateCheckDialog.PERMANENT) {
                addCertToTrustStore(certs);
            }
        }
    }

    private void addCertToTrustStore(X509Certificate[] certs) {
        if(userkeystore != null && certs != null && certs.length > 0) {
            try {
                X509Certificate lastCert = certs[certs.length - 1];
                String dn = lastCert.getSubjectX500Principal().getName();
                BigInteger serial = lastCert.getSerialNumber();
                String proposedName = dn + "#" + serial.toString(16);
                String realName = proposedName;
                if(userkeystore.containsAlias(realName)) {
                    Integer count = 0;
                    while(userkeystore.containsAlias(realName)) {
                        realName = proposedName + "#" + Integer.toString(count);
                    }
                }
                userkeystore.setCertificateEntry(realName, lastCert);
            } catch (KeyStoreException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    private boolean isLastCertificateTrusted(X509Certificate[] certs) {
        try {
            if (certs == null || certs.length == 0) {
                return false;
            }
            X509Certificate lastCert = certs[certs.length - 1];
            if (userkeystore != null
                    && userkeystore.getCertificateAlias(lastCert) != null) {
                return true;
            }
        } catch (KeyStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }
}
