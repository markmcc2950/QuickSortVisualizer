void main() {
    try {
        SoundHandler.init();
    } catch (Exception e) {
        // Don't interrupt - just log and continue without sound
        System.err.println("Sound initialization failed: " + e.getMessage());
    }

    // Clean up sound resources on exit
    Runtime.getRuntime().addShutdownHook(new Thread(SoundHandler::shutdown));

    WindowHandler.createWindow();
}