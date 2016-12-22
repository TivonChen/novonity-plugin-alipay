//
//  AlipayModule.h
//
//  Created by ChenTivon on 14/11/15.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>
#import <AlipaySDK/AlipaySDK.h>

@interface AlipayModule : CDVPlugin

- (void)pay: (CDVInvokedUrlCommand*)command;

@end
