import { WebPlugin } from "@capacitor/core";
import { CameraOverlayPlugin } from "./definitions";
export declare class CameraOverlayWeb extends WebPlugin implements CameraOverlayPlugin {
    constructor();
    startCamera(): Promise<void>;
    stopCamera(): Promise<void>;
}
declare const CameraOverlay: CameraOverlayWeb;
export { CameraOverlay };
