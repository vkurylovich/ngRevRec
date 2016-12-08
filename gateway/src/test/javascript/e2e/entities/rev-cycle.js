'use strict';

describe('RevCycle e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load RevCycles', function () {
        entityMenu.click();
        element.all(by.css('[ui-sref="rev-cycle"]')).first().click().then(function() {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/Rev Cycles/);
        });
    });

    it('should load create RevCycle dialog', function () {
        element(by.css('[ui-sref="rev-cycle.new"]')).click().then(function() {
            expect(element(by.css('h4.modal-title')).getText()).toMatch(/Create or edit a Rev Cycle/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
