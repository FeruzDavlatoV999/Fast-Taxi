import PassKit
import StoreKit

@objc class ApplePayHandler: NSObject {
    private var paymentController: PKPaymentAuthorizationController?
    private var paymentStatus = PKPaymentAuthorizationStatus.failure
    private var completionHandler: ((Bool) -> Void)?
    
    static let supportedNetworks: [PKPaymentNetwork] = [
        .visa,
        .masterCard,
        .amex
    ]
    
    func canMakePayments() -> Bool {

        return PKPaymentAuthorizationController.canMakePayments() &&
               PKPaymentAuthorizationController.canMakePayments(usingNetworks: ApplePayHandler.supportedNetworks)
    }
    
    func startPayment(amount: Double, completion: @escaping (Bool) -> Void) {
        let paymentRequest = PKPaymentRequest()
        paymentRequest.merchantIdentifier = "merchant.uz.makontv.mobile"
        paymentRequest.supportedNetworks = ApplePayHandler.supportedNetworks
        paymentRequest.merchantCapabilities = .capability3DS
        paymentRequest.countryCode = "UZ"
        paymentRequest.currencyCode = "UZS"
        
        let total = PKPaymentSummaryItem(
            label: "Makon TV Subscription",
            amount: NSDecimalNumber(value: amount)
        )
        
        paymentRequest.paymentSummaryItems = [total]
        
        paymentController = PKPaymentAuthorizationController(paymentRequest: paymentRequest)
        paymentController?.delegate = self
        completionHandler = completion
        
        paymentController?.present { presented in
            if !presented {
                self.completionHandler?(false)
            }
        }
    }
}

extension ApplePayHandler: PKPaymentAuthorizationControllerDelegate {
    func paymentAuthorizationController(_ controller: PKPaymentAuthorizationController,
                                      didAuthorizePayment payment: PKPayment,
                                      handler completion: @escaping (PKPaymentAuthorizationResult) -> Void) {
        
        
        self.paymentStatus = .success
        completion(PKPaymentAuthorizationResult(status: self.paymentStatus, errors: nil))
    }
    
    func paymentAuthorizationControllerDidFinish(_ controller: PKPaymentAuthorizationController) {
        controller.dismiss {
            DispatchQueue.main.async {
                self.completionHandler?(self.paymentStatus == .success)
            }
        }
    }
}
