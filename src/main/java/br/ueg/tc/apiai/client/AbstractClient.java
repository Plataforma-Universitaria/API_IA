package br.ueg.tc.apiai.client;

public abstract class AbstractClient implements Client {
        public abstract String getApiKey();
        public abstract String getModel();
}