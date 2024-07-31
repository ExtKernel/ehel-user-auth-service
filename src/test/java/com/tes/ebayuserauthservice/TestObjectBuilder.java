package com.tes.ebayuserauthservice;

import com.tes.ebayuserauthservice.model.EbayUser;

public class TestObjectBuilder {
    public EbayUser buildTestEbayUser() {
        return new EbayUser(
                "test-username",
                "test-client-id",
                "test-client-secret",
                "test-redirect-url"
        );
    }
}
