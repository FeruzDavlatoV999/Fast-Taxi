import Foundation
import ComposeApp

class PaymentPlatform: NSObject, PaymentPlatformInterface {
    
    
    private let applePayHandler = ApplePayHandler()
    
    
    public func isApplePayAvailable() -> Bool {
        return applePayHandler.canMakePayments()
    }
    
    public func processPayment(amount: Double, completion: @escaping (KotlinBoolean) -> Void) {
       if applePayHandler.canMakePayments() {
            applePayHandler.startPayment(amount: amount) { kotlinResult in completion(KotlinBoolean(value: kotlinResult))
            }
       } else {
           completion(KotlinBoolean(value: false))
       }
    }
    
    
}
