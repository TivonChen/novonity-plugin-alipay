//
//  AlipayModule.m
//
//  Created by ChenTivon on 14/11/15.
//

#import "AlipayModule.h"

const NSString* scheme = @"alisdkmayi";

@implementation AlipayModule

- (void) pay: (CDVInvokedUrlCommand*)command {
    NSString* args = [command.arguments objectAtIndex: 0];

    if (args == nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"arg was null"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    NSLog(@"alipay orderinfo = %@", args);

    [[AlipaySDK defaultService] payOrder:args fromScheme:(NSString*)scheme callback:^(NSDictionary *resultDic) {
        NSLog(@"AlipayModule sendPayRequest result = %@", resultDic);
        NSString* result = [resultDic objectForKey:@"resultStatus"];
        NSLog(@"AlipayModule sendPayRequest resultStatus = %@", result);
        // success: resultStatus == 9000
        CDVPluginResult* pluginResult = nil;
        if ([result isEqualToString: @"9000"]) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"支付成功"];
        } else if ([result isEqualToString:@"8000"]) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"支付结果确认中"];
        } else if ([result isEqualToString:@"6001"]) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"支付取消"];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"支付失败"];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)handleOpenURL:(NSNotification*)notification {
    NSURL* url = [notification object];

    NSLog(@"AlipayModule handleOpenURL url.scheme = %@", url.scheme);

    if ([url isKindOfClass:[NSURL class]] && [url.scheme isEqualToString:(NSString*)scheme]) {
        [[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
            NSLog(@"AlipayModule processOrderWithPaymentResult result = %@",resultDic);
        }];
    }
}

@end
