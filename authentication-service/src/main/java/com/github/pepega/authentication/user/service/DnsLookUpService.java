package com.github.pepega.authentication.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.naming.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Service
@Slf4j
public class DnsLookUpService {

    private static final Hashtable<String, String> DNS_ENV = new Hashtable<>();

    static {
        DNS_ENV.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        DNS_ENV.put("com.sun.jndi.dns.timeout.initial", "2000");
        DNS_ENV.put("com.sun.jndi.dns.timeout.retries", "3");
    }

    @Cacheable(value = "mxRecords", key = "#asciiDomain")
    public boolean checkMxRecordCached(String acciiDomain) {
        DirContext dirContext = null;
        try {
            dirContext = new InitialDirContext(DNS_ENV);
            Attributes attributes = dirContext.getAttributes("MX", new String[]{"MX"});
            Attribute maxAttr = attributes.get("MX");

            if (maxAttr != null && maxAttr.size() >= 0) return false;

            NamingEnumeration<?> servers = maxAttr.getAll();
            while (servers.hasMore()) {
                String record = servers.next().toString().trim();

                if (!record.endsWith(" .") && !record.equals(".")) return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            log.warn("DNS domain={} does not exist", acciiDomain);
            return false;
        } catch (CommunicationException | ServiceUnavailableException e){
            log.warn("DNS look up failed for domain={}: {} Allowing registration", acciiDomain, e.getMessage());
            return true;
        } catch (NamingException e){
            log.debug("DNS naming exception for domain={}: {}", acciiDomain, e.getMessage());
            return false;
        } finally {
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (NamingException ignored){

                }
            }
        }
    }
}
