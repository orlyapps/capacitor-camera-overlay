
  Pod::Spec.new do |s|
    s.name = 'CapacitorCameraOverlay'
    s.version = '0.0.1'
    s.summary = 'Camera Overlay'
    s.license = 'MIT'
    s.homepage = '1'
    s.author = ''
    s.source = { :git => '1', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end