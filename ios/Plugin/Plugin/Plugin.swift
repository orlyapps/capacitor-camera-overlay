import Foundation
import Capacitor
import AVFoundation
import UIKit
/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CameraOverlay)
public class CameraOverlay: CAPPlugin, AVCaptureVideoDataOutputSampleBufferDelegate {
    
    var previewView : UIView!
    var boxView:UIView!
    let myButton: UIButton = UIButton()
    
    //Camera Capture requiered properties
    var videoDataOutput: AVCaptureVideoDataOutput!
    var videoDataOutputQueue: DispatchQueue!
    var previewLayer:AVCaptureVideoPreviewLayer!
    var captureDevice : AVCaptureDevice!
    let session = AVCaptureSession()
    
    
    @objc func startCamera(_ call: CAPPluginCall) {
        
        
        DispatchQueue.main.async {
            self.previewView = UIView(frame: CGRect(x: 0,
                                               y: 0,
                                               width: UIScreen.main.bounds.size.width,
                                               height: UIScreen.main.bounds.size.height))
            self.previewView.contentMode = UIView.ContentMode.scaleAspectFit
            self.webView.superview?.addSubview(self.previewView);
            self.webView.superview?.bringSubviewToFront(self.webView);
       
            self.webView.isOpaque = false
            self.webView.backgroundColor = UIColor.clear
            self.webView.scrollView.backgroundColor = UIColor.clear
            
            
            
            self.setupAVCapture()
        }
        call.resolve();
        
        

    }
    @objc func stopCamera(_ call: CAPPluginCall) {
        session.stopRunning()
        DispatchQueue.main.async {
            self.previewView.removeFromSuperview();
        }
        call.resolve();
        
    }
    
    func setupAVCapture(){
        session.sessionPreset = AVCaptureSession.Preset.high
        guard let device = AVCaptureDevice
            .default(AVCaptureDevice.DeviceType.builtInWideAngleCamera,
                     for: .video,
                     position: AVCaptureDevice.Position.front) else {
                        return
        }
        captureDevice = device
        beginSession()
    }
    func beginSession(){
        var deviceInput: AVCaptureDeviceInput!
        
        do {
            deviceInput = try AVCaptureDeviceInput(device: captureDevice)
            guard deviceInput != nil else {
                print("error: cant get deviceInput")
                return
            }
            
            if self.session.canAddInput(deviceInput){
                self.session.addInput(deviceInput)
            }
            
            videoDataOutput = AVCaptureVideoDataOutput()
            videoDataOutput.alwaysDiscardsLateVideoFrames=true
            videoDataOutputQueue = DispatchQueue(label: "VideoDataOutputQueue")
            videoDataOutput.setSampleBufferDelegate(self, queue:self.videoDataOutputQueue)
            
            if session.canAddOutput(self.videoDataOutput){
                session.addOutput(self.videoDataOutput)
            }
            
            videoDataOutput.connection(with: .video)?.isEnabled = true
            
            previewLayer = AVCaptureVideoPreviewLayer(session: self.session)
            previewLayer.videoGravity = AVLayerVideoGravity.resizeAspect
            
            let rootLayer :CALayer = self.previewView.layer
            rootLayer.masksToBounds=true
            previewLayer.frame = rootLayer.bounds
            rootLayer.addSublayer(self.previewLayer)
            session.startRunning()
        } catch let error as NSError {
            deviceInput = nil
            print("error: \(error.localizedDescription)")
        }
    }
    
    func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        // do stuff here
    }
    
    // clean up AVCapture
    func stopCamera(){
        session.stopRunning()
    }
}
