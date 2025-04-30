package com.example.spectacleapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spectacleapp.Models.Reservation;
import com.example.spectacleapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private Context context;
    private List<Reservation> reservationList;

    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView textSpectacleTitle, textDateTime, textTotalPrice, textSeats;
        Button buttonAction;

        public ViewHolder(View view) {
            super(view);
            imagePoster = view.findViewById(R.id.imagePoster);
            textSpectacleTitle = view.findViewById(R.id.textSpectacleTitle);
            textDateTime = view.findViewById(R.id.textDateTime);
            textTotalPrice = view.findViewById(R.id.textTotalPrice);
            textSeats = view.findViewById(R.id.textSeats);
            buttonAction = view.findViewById(R.id.buttonAction);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.textSpectacleTitle.setText(reservation.getSpectacleTitle());
        holder.textDateTime.setText(reservation.getDate() + " - " + reservation.getTime());
        holder.textTotalPrice.setText("Total: " + reservation.getTotalPrice() + " Dinars");
        holder.textSeats.setText("Seats: " + String.join(", ", reservation.getSeats()));
        Glide.with(context).load(reservation.getPoster()).into(holder.imagePoster); // Load image from URL

        holder.buttonAction.setOnClickListener(v -> {
            // Get the reservation content to generate QR code
            String reservationContent = "Spectacle Title: " + reservation.getSpectacleTitle()
                    + "\nDate: " + reservation.getDate()
                    + "\nTime: " + reservation.getTime()
                    + "\nSeats: " + String.join(", ", reservation.getSeats());

            // Generate QR code
            Bitmap qrCodeBitmap = generateQRCode(reservationContent);

            if (qrCodeBitmap != null) {
                // Generate PDF with the QR code
                generatePDFWithQRCode(qrCodeBitmap);
            } else {
                Toast.makeText(context, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Generate QR Code as Bitmap
    private Bitmap generateQRCode(String content) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 500, 500);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Generate PDF with QR Code
    private void generatePDFWithQRCode(Bitmap qrCodeBitmap) {
        try {
            // Create file for PDF
            File pdfFile = new File(context.getExternalFilesDir(null), "ticket.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

            // Create PdfWriter instance
            PdfWriter writer = new PdfWriter(fileOutputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Create Document instance
            Document document = new Document(pdfDoc);

            // Convert Bitmap to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();

            // Create ImageData from byte array
            ImageData qrImageData = ImageDataFactory.create(qrCodeBytes);

            // Create Image object from ImageData
            Image qrImage = new Image(qrImageData);

            // Scale the image to fit within 200x200 (adjust this as necessary)
            qrImage.scaleToFit(200, 200);  // Scaling the QR code

            // Set the position of the image on the PDF
            qrImage.setFixedPosition(100, 500); // Position (100px, 500px) on the page

            // Add image to the document
            document.add(qrImage);

            // Close document
            document.close();

            // Close file output stream
            fileOutputStream.close();

            // Optionally, open the PDF with a viewer
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return reservationList.size();
    }
}
