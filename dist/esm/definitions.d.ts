declare global {
    interface PluginRegistry {
        CameraOverlay?: CameraOverlayPlugin;
    }
}
export interface CameraOverlayPlugin {
    startCamera(): Promise<void>;
    stopCamera(): Promise<void>;
}
