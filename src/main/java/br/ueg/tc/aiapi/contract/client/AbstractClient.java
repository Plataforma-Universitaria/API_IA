package br.ueg.tc.aiapi.contract.client;

public abstract class AbstractClient implements Client {
        public abstract String getApiKey();
        public abstract String getModel();
}