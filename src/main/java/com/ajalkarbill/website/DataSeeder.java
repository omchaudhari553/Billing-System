package com.ajalkarbill.website;

import com.ajalkarbill.website.entity.*;
import com.ajalkarbill.website.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FeatureRepository featureRepo;
    private final ModuleRepository moduleRepo;
    private final TestimonialRepository testimonialRepo;
    private final FAQRepository faqRepo;
    private final DownloadInfoRepository downloadInfoRepo;

    public DataSeeder(FeatureRepository featureRepo, ModuleRepository moduleRepo, TestimonialRepository testimonialRepo, FAQRepository faqRepo, DownloadInfoRepository downloadInfoRepo) {
        this.featureRepo = featureRepo;
        this.moduleRepo = moduleRepo;
        this.testimonialRepo = testimonialRepo;
        this.faqRepo = faqRepo;
        this.downloadInfoRepo = downloadInfoRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (featureRepo.count() == 0) {
            featureRepo.saveAll(List.of(
                    new Feature("Fast Billing", "Generate GST and non-GST bills in seconds with keyboard shortcuts.", "bi-receipt"),
                    new Feature("Easy Inventory", "Track stock levels in real-time, get low stock alerts and barcode scanning.", "bi-box-seam"),
                    new Feature("GST Ready", "Auto-calculate GST, generate GSTR reports and E-Way bills easily.", "bi-calculator"),
                    new Feature("Business Reports", "Get detailed insights with 50+ reports for sales, profit, and taxes.", "bi-graph-up"),
                    new Feature("Secure Data", "Automatic cloud backup and enterprise-grade security for your data.", "bi-shield-check"),
                    new Feature("User Friendly", "Intuitive design that requires no accounting knowledge to operate.", "bi-emoji-smile")
            ));
        }

        if (moduleRepo.count() == 0) {
            moduleRepo.saveAll(List.of(
                    new com.ajalkarbill.website.entity.Module("Billing", "Create professional invoices, quotes, and delivery challans.", "bi-file-earmark-text"),
                    new com.ajalkarbill.website.entity.Module("Inventory", "Manage products, categories, units, and barcode generation.", "bi-inboxes"),
                    new com.ajalkarbill.website.entity.Module("Customers", "Track customer balances, credit limits, and payment history.", "bi-people"),
                    new com.ajalkarbill.website.entity.Module("Suppliers", "Manage vendor accounts, purchase orders, and unpaid bills.", "bi-truck"),
                    new com.ajalkarbill.website.entity.Module("Expenses", "Record daily business expenses and categorize them for analysis.", "bi-cash-coin"),
                    new com.ajalkarbill.website.entity.Module("Reports", "Export GSTR, Profit/Loss, and Stock summary in Excel or PDF.", "bi-file-earmark-bar-graph")
            ));
        }

        if (testimonialRepo.count() == 0) {
            testimonialRepo.saveAll(List.of(
                    new Testimonial("Rajesh Kumar", "Rajesh Electronics", "AjalkarBill transformed how I manage my store. Billing is now 5x faster and inventory is always accurate.", 5, "https://randomuser.me/api/portraits/men/32.jpg"),
                    new Testimonial("Priya Sharma", "Sharma Supermarket", "The best software for retail businesses. The reports feature helps me understand my daily profit easily.", 5, "https://randomuser.me/api/portraits/women/44.jpg"),
                    new Testimonial("Amit Patel", "Patel Hardware", "Very easy to use even for my staff. GST filing is no longer a headache. Highly recommended!", 4, "https://randomuser.me/api/portraits/men/68.jpg")
            ));
        }

        if (faqRepo.count() == 0) {
            faqRepo.saveAll(List.of(
                    new FAQ("What is AjalkarBill?", "AjalkarBill is a comprehensive billing, inventory, and accounting software designed specifically for retail and wholesale businesses in India."),
                    new FAQ("Can I use GST Billing?", "Yes, AjalkarBill is 100% GST ready. You can create GST invoices, calculate taxes automatically, and generate GSTR reports."),
                    new FAQ("Can I manage inventory?", "Absolutely. You can track stock levels, set low stock alerts, manage multiple warehouses, and use barcode scanners."),
                    new FAQ("Does it work offline?", "Yes, AjalkarBill is a desktop application that works completely offline. Your data is stored securely on your computer."),
                    new FAQ("How do I update?", "Updates are provided automatically via the internet when you are connected. You'll receive a notification when a new version is available.")
            ));
        }

        if (downloadInfoRepo.count() == 0) {
            downloadInfoRepo.save(new DownloadInfo("v2.5.0", "https://download.ajalkarbill.com/windows", "June 2026", "Windows 10/11 (64-bit)"));
        }
    }
}
