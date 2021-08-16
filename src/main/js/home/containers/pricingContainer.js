import React, { Component } from 'react';
import '../../common/styles/style.css';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

class pricingContainer extends Component {

	componentDidMount = () => {
	    const script = document.createElement("script");
	    script.src = "https://static-na.payments-amazon.com/OffAmazonPayments/us/js/Widgets.js";
	    script.async = true;
	    document.body.appendChild(script);
	}
	

  render() {
    return (<div >
      <div className="priceing-table w3l">
        <div className="wrap">
          <center><img src="./icon/logotrans.png" /></center>
      	<div style={{backgroundColor:'#1A5276'}}>
		<h1><font color="white">Pricing Plans</font></h1>
		</div>
          <div className="priceing-table-main">
            <div className="price-grid">
              <div className="price-block agile">
                <div className="price-gd-top pric-clr1">
                  <h4>Starter</h4>
                  <h3>$25</h3>
                  <h5>2 Tests</h5>
                </div>
                <div className="price-gd-bottom">
                  <div className="price-list">
                    <ul>
                  <li>24/7 Email Support</li>
                  <li>1 User Dashboard</li>
                  <li>Technical Support</li>
                  <li>1 Manager Login</li>
                  <li>-------</li>
                </ul>
                </div>
                </div>
                <div className="price-selet pric-sclr1">
                  <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
                    <input type="hidden" name="cmd" value="_s-xclick" />
                    <input type="hidden" name="hosted_button_id" value="" />
                    <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!" />
                    <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1" />
                  </form>
                   <br/>
                    <div
                    data-ap-widget-type="expressPaymentButton"
                    data-ap-signature=""
                    data-ap-seller-id=""
                    data-ap-access-key=""
                    data-ap-lwa-client-id=""
                    data-ap-return-url=""
                    data-ap-currency-code="USD"
                    data-ap-amount="25"
                    data-ap-note="STARTER PLAN- 1 MANAGER LOGIN"
                    data-ap-shipping-address-required="true"
                    data-ap-payment-action="AuthorizeAndCapture"/>
                </div>
              </div>
            </div>
            <div className="price-grid">
              <div className="price-block agile">
                <div className="price-gd-top pric-clr2">
                  <h4>Global</h4>
                  <h3>$55</h3>
                  <h5>5 Tests</h5>
                </div>
                <div className="price-gd-bottom">
                  <div className="price-list">
                    <ul>
                  <li>24/7 Email Support</li>
                  <li>1 User Dashboard</li>
                  <li>Technical Support</li>
                  <li>1 Manager Login</li>
                  <li>-------</li>
                </ul>
                  </div>
                </div>
                <div className="price-selet pric-sclr2">
                  <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
                    <input type="hidden" name="cmd" value="_s-xclick" />
                    <input type="hidden" name="hosted_button_id" value="" />
                    <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!" />
                    <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1" />
                  </form>
                    <br/>
                    <div
                    data-ap-widget-type="expressPaymentButton"
                    data-ap-signature=""
                    data-ap-seller-id=""
                    data-ap-access-key=""
                    data-ap-lwa-client-id=""
                    data-ap-return-url=""
                    data-ap-currency-code="USD"
                    data-ap-amount="55"
                    data-ap-note="5 TESTS - 1 MANAGER LOGIN"
                    data-ap-shipping-address-required="false"
                    data-ap-payment-action="Authorize"/>
                </div>
              </div>
            </div>
            <div className="price-grid wthree">
              <div className="price-block agile">
                <div className="price-gd-top pric-clr3">
                  <h4>Enterprise</h4>
                  <h3>$100</h3>
                  <h5>10 Tests</h5>
                </div>
                <div className="price-gd-bottom">
                  <div className="price-list">
                    <ul>
                  <li>24/7 Email Support</li>
                  <li>2 User Dashboard</li>
                  <li>Technical Support</li>
                  <li>2 Manager Logins</li>
                  <li>1 Custom Test</li>
                </ul>
                  </div>
                </div>
                <div className="price-selet pric-sclr3">
                  <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
                    <input type="hidden" name="cmd" value="_s-xclick" />
                    <input type="hidden" name="hosted_button_id" value="" />
                    <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!" />
                    <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1" />
                  </form>
                  <br/>
                    <div
                    data-ap-widget-type="expressPaymentButton"
                    data-ap-signature=""
                    data-ap-seller-id=""
                    data-ap-access-key=""
                    data-ap-lwa-client-id=""
                    data-ap-return-url=""
                    data-ap-currency-code="USD"
                    data-ap-amount="100"
                    data-ap-note="ENTERPRISE PLAN-10 TESTS"
                    data-ap-shipping-address-required="false"
                    data-ap-payment-action="AuthorizeAndCapture"/>
                </div>
              </div>
            </div>
            <div className="clear" />
          </div>
        </div>
        <div className="priceing-table-main">
          <div className="price-grid">
            <div className="price-block agile">
              <div className="price-gd-top pric-clr1">
                <h4>Deluxe</h4>
                <h3>$150</h3>
                <h5>15 Tests</h5>
              </div>
              <div className="price-gd-bottom">
                <div className="price-list">
                  <ul>
                    <li>24/7 Email Support</li>
                    <li>3 User Dashboard</li>
                    <li>Technical Support</li>
                    <li>3 Manager Logins</li>
                    <li>4 Custom Test</li>
                  </ul>
                </div>
              </div>
              <div className="price-selet pric-sclr1">
                <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
                  <input type="hidden" name="cmd" value="_s-xclick" />
                  <input type="hidden" name="hosted_button_id" value="" />
                  <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!" />
                  <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1" />
                </form>
                <br/>
                  <div
                  data-ap-widget-type="expressPaymentButton"
                  data-ap-signature=""
                  data-ap-seller-id=""
                  data-ap-access-key=""
                  data-ap-lwa-client-id=""
                  data-ap-return-url=""
                  data-ap-currency-code="USD"
                  data-ap-amount="150"
                  data-ap-note="DELUXE PLAN - 15 TESTS"
                  data-ap-shipping-address-required="false"
                  data-ap-payment-action="AuthorizeAndCapture"/>
              </div>
            </div>
          </div>
          <div className="price-grid">
            <div className="price-block agile">
              <div className="price-gd-top pric-clr2">
                <h4>Ultra</h4>
                <h3>$375</h3>
                <h5>35 Tests</h5>
              </div>
              <div className="price-gd-bottom">
                <div className="price-list">
                  <ul>
                    <li>24/7 Email Support</li>
                    <li>5 User Dashboard</li>
                    <li>Technical Support</li>
                    <li>5 Manager Logins</li>
                    <li>4 Custom Test</li>
                  </ul>
                </div>
              </div>
              <div className="price-selet pric-sclr2">
                <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
                  <input type="hidden" name="cmd" value="_s-xclick" />
                  <input type="hidden" name="hosted_button_id" value="" />
                  <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!" />
                  <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1" />
                </form>
                 <br/>
                 <div
                 data-ap-widget-type="expressPaymentButton"
                 data-ap-signature=""
                 data-ap-seller-id=""
                 data-ap-access-key=""
                 data-ap-lwa-client-id=""
                 data-ap-return-url=""         
                 data-ap-currency-code="USD"
                 data-ap-amount="375"
                 data-ap-note="ULTRA PLAN - 35 TESTS "
                 data-ap-shipping-address-required="false"
                 data-ap-payment-action="AuthorizeAndCapture" />
              </div>
            </div>
          </div>
          <div className="price-grid wthree">
            <div className="price-block agile">
              <div className="price-gd-top pric-clr3">
                <h4>Ultra Premium</h4>
                <h3>Custom Plan</h3>
                <h5>Contact Sales</h5>
              </div>
              <div className="price-gd-bottom">
                <div className="price-list">
                  <ul>
                    <li>---------</li>
                    <li>---------</li>
                    <li>---------</li>
                    <li>---------</li>
                    <li>---------</li>
                  </ul>
                </div>              
              </div>
            </div>
          </div>
          <div className="clear" />
        </div>
      </div>
      <div id="popup">
        <div id="small-dialog" className="mfp-hide">
          <div className="pop_up">
            <div className="payment-online-form-left">
              <form action="#" method="post">
                <h4>Sign Up</h4>
                <ul>
                  <li><input className="text-box-dark" type="text" placeholder="Name" name="Name" required="" /></li>
                  <li><input className="text-box-dark" type="text" placeholder="Email" name="Email" /></li>
                  <li><input className="text-box-dark" type="password" placeholder="Password" name="Password" /></li>
                  <li><input className="text-box-dark" type="text" placeholder="Phone" name="Phone" /></li>
                </ul>
                <span className="checkbox1">
                  <label className="checkbox"><input type="checkbox" name="" checked="" /><i />I Accept Terms.</label>
                </span>
                <ul className="payment-sendbtns">
                  <li><input type="submit" value="Submit" /></li>
                </ul>
              </form>
            </div>
          </div>
        </div>
      </div>
            </div>);
  }
}

function mapStateToProps(state) {
  return {
  };
}

export default withRouter(connect(mapStateToProps)(pricingContainer));
