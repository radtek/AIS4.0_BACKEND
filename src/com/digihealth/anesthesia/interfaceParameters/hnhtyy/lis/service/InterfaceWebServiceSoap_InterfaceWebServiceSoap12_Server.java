package com.digihealth.anesthesia.interfaceParameters.hnhtyy.lis.service;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 3.1.4
 * 2018-11-30T11:36:27.763+08:00
 * Generated source version: 3.1.4
 * 
 */
 
public class InterfaceWebServiceSoap_InterfaceWebServiceSoap12_Server{

    protected InterfaceWebServiceSoap_InterfaceWebServiceSoap12_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new InterfaceWebServiceSoap12Impl();
        String address = "http://192.168.0.124:81/default.asmx";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new InterfaceWebServiceSoap_InterfaceWebServiceSoap12_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
